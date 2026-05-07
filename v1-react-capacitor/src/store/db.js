import { openDB } from 'idb';

const DB_NAME = 'NaamSmaranDB';
const DB_VERSION = 1;

export async function initDB() {
  return openDB(DB_NAME, DB_VERSION, {
    upgrade(db) {
      // DailyRecord table: stores each day's stats, checkmarks, etc.
      // id: "YYYY-MM-DD"
      if (!db.objectStoreNames.contains('DailyRecord')) {
        db.createObjectStore('DailyRecord', { keyPath: 'id' });
      }
      
      // AppSettings table: stores a single settings record.
      // id: 1
      if (!db.objectStoreNames.contains('AppSettings')) {
        db.createObjectStore('AppSettings', { keyPath: 'id' });
      }
      
      // Quotes table: tracks when quotes were last shown to avoid repeating within 7 days.
      // id: string (e.g., "chaturasi_01")
      if (!db.objectStoreNames.contains('Quotes')) {
        db.createObjectStore('Quotes', { keyPath: 'id' });
      }
    },
  });
}

// Basic CRUD wrapper functions
export async function getRecord(storeName, id) {
  const db = await initDB();
  return db.get(storeName, id);
}

export async function getAllRecords(storeName) {
  const db = await initDB();
  return db.getAll(storeName);
}

export async function putRecord(storeName, record) {
  const db = await initDB();
  return db.put(storeName, record);
}

export async function deleteRecord(storeName, id) {
  const db = await initDB();
  return db.delete(storeName, id);
}

export async function clearStore(storeName) {
  const db = await initDB();
  return db.clear(storeName);
}
