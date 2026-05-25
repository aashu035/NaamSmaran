import { create } from 'zustand';
import { getRecord, putRecord, getAllRecords, clearStore } from './db';
import { getSpiritualDate } from '../logic/dayBoundary';
import { calculateNextTarget } from '../logic/targetEngine';
import { calculateStreak } from '../logic/streakEngine';

const DEFAULT_SETTINGS = {
  id: 1,
  activeTheme: "sharad-moon",
  chosenMantraText: "राधा",
  deityDisplayName: "जय जय श्री हित हरिवंश",
  fontStyle: "traditional",
  backgroundMode: "3d",
  backgroundImageCustom: "",
  audioFileName: "",
  audioVolume: 0.7,
  audioLoop: true,
  reminderMorningEnabled: true,
  reminderMorningTime: "04:00",
  reminderEveningEnabled: true,
  reminderEveningTime: "20:00",
  reminderDayEndEnabled: true,
  reminderDayEndTime: "22:00",
  reminderProgressEnabled: true,
  reminderProgressTime: "14:00",
  reminderEncouragementEnabled: true,
  initialTarget: 21600,
  targetIncrement: 1000,
  dayBoundaryHour: 3,
  autoBackupEnabled: true,
  lastBackupTimestamp: null,
  useIndianNumbering: true
};

function createEmptyRecord(dateStr, target) {
  const [year, month, day] = dateStr.split('-');
  const d = new Date(parseInt(year), parseInt(month) - 1, parseInt(day));
  const days = ['रविवार', 'सोमवार', 'मंगलवार', 'बुधवार', 'गुरुवार', 'शुक्रवार', 'शनिवार'];
  const dayOfWeek = days[d.getDay()];

  return {
    id: dateStr,
    date: dateStr,
    dayOfWeek,
    target: target,
    did: 0,
    nextDayTarget: null,
    checkNaamJap: false,
    checkChaturasi: false,
    checkMaansikSeva: false,
    checkPrarthana: false,
    checkBhaktMaal: false,
    checkCharnamrit: false,
    streakCount: 0,
    entryTimestamp: Date.now(),
    quoteShown: null,
    notes: "",
    createdAt: Date.now(),
    updatedAt: Date.now()
  };
}

export const useAppStore = create((set, get) => ({
  settings: null,
  todayRecord: null,
  isLoaded: false,

  initStore: async () => {
    // 1. Load Settings
    let settings = await getRecord('AppSettings', 1);
    if (!settings) {
      settings = DEFAULT_SETTINGS;
      await putRecord('AppSettings', settings);
    }

    set({ settings });

    // 2. Load or Create Today's Record
    await get().loadTodayRecord();

    set({ isLoaded: true });
  },

  loadTodayRecord: async () => {
    const { settings } = get();
    const todayStr = getSpiritualDate(new Date(), settings.dayBoundaryHour);

    let record = await getRecord('DailyRecord', todayStr);

    if (!record) {
      // Need to find the last known record to compute missed days
      const allRecords = await getAllRecords('DailyRecord');
      allRecords.sort((a, b) => a.id.localeCompare(b.id)); // sort chronologically

      let lastTarget = settings.initialTarget;
      let lastStreak = 0;
      let lastDateObj = null;

      if (allRecords.length > 0) {
        const lastRecord = allRecords[allRecords.length - 1];
        lastTarget = lastRecord.nextDayTarget || calculateNextTarget(lastRecord.target, lastRecord.did, settings.targetIncrement);
        lastStreak = lastRecord.streakCount;
        lastDateObj = new Date(lastRecord.id);
      }

      // If there's a gap between last record and today, fill it with missing records (D=0)
      if (lastDateObj) {
        let currentDate = new Date(lastDateObj);
        currentDate.setDate(currentDate.getDate() + 1);

        const todayDateObj = new Date(todayStr);

        while (currentDate < todayDateObj) {
          const gapStr = currentDate.toISOString().split('T')[0];
          const gapRecord = createEmptyRecord(gapStr, lastTarget);
          gapRecord.streakCount = 0; // missed day means streak is 0
          gapRecord.nextDayTarget = calculateNextTarget(lastTarget, 0, settings.targetIncrement);

          await putRecord('DailyRecord', gapRecord);

          lastTarget = gapRecord.nextDayTarget;
          lastStreak = 0;
          currentDate.setDate(currentDate.getDate() + 1);
        }
      }

      record = createEmptyRecord(todayStr, lastTarget);
      record.streakCount = calculateStreak(lastStreak, lastTarget, 0); // At 0 count, streak is 0 unless target is 0?

      await putRecord('DailyRecord', record);
    }

    set({ todayRecord: record });
  },

  updateSetting: async (key, value) => {
    const settings = { ...get().settings, [key]: value };
    await putRecord('AppSettings', settings);
    set({ settings });
  },

  updateTodayRecord: async (updates) => {
    const { todayRecord, settings } = get();
    if (!todayRecord) return;

    const newRecord = {
      ...todayRecord,
      ...updates,
      updatedAt: Date.now()
    };

    // Auto-check checkNaamJap if did > 0
    if (newRecord.did > 0) {
      newRecord.checkNaamJap = true;
    }

    // Determine current streak dynamically (for UI rendering if needed)
    // Actually, streakCount is updated at end of day, but we can compute it live.
    // Wait, AGENTS.md says streakCount is stored for fast reads. We need previous day's streak.

    await putRecord('DailyRecord', newRecord);
    set({ todayRecord: newRecord });
  },

  addJapCount: async (amount) => {
    const { todayRecord } = get();
    if (!todayRecord) return;

    await get().updateTodayRecord({ did: todayRecord.did + amount });
  },

  resetAllData: async () => {
    await clearStore('DailyRecord');
    await clearStore('Quotes');
    await putRecord('AppSettings', DEFAULT_SETTINGS);
    set({ settings: DEFAULT_SETTINGS, todayRecord: null });
    await get().loadTodayRecord();
  }
}));
