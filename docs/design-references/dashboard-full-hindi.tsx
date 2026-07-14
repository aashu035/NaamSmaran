import React from 'react';
import { 
  Flower2, ChevronDown, Check, CircleDot,
  BookOpen, Feather, Flame, Landmark, Eye,
  Home, BarChart2, Target, User, Plus,
  ChevronRight, Activity
} from 'lucide-react';
import { 
  AreaChart, Area, PieChart, Pie, Cell, ResponsiveContainer, Tooltip
} from 'recharts';

// --- Mock Data ---
const weeklyData = [
  { day: 'सोम', date: '22 मई', state: 'done' },
  { day: 'मंगल', date: '23 मई', state: 'done' },
  { day: 'बुध', date: '24 मई', state: 'done' },
  { day: 'गुरु', date: '25 मई', state: 'done' },
  { day: 'शुक्र', date: '26 मई', state: 'done' },
  { day: 'शनि', date: '27 मई', state: 'partial' },
  { day: 'रवि', date: 'आज', state: 'empty' },
];

const trendData = [
  { day: '28 अप्रैल', value: 200000 }, { day: '', value: 250000 }, { day: '', value: 300000 },
  { day: '5 मई', value: 280000 }, { day: '', value: 400000 }, { day: '', value: 450000 },
  { day: '12 मई', value: 420000 }, { day: '', value: 550000 }, { day: '', value: 600000 },
  { day: '19 मई', value: 580000 }, { day: '', value: 700000 }, { day: '', value: 750000 },
  { day: 'आज', value: 876540 }
];

const donutData = [
  { name: 'लक्ष्य पूरे किए', value: 22, color: '#E8388B' }, // Pink
  { name: 'आंशिक', value: 5, color: '#F39C12' }, // Orange
  { name: 'लक्ष्य नहीं पूरे हुए', value: 4, color: '#5B86E5' }, // Blue
];

const sadhanas = [
  { id: 1, name: 'नाम जप\n(द्वि-ट्रैक)', total: '8,76,540', sub: 'of 10,50,000', label2: '73 माला', sub2: 'of 84 माला', pct: 83, pct2: 87, icon: CircleDot, color: '#F39C12', status: '87%' },
  { id: 2, name: 'श्री हित\nचतुरसी जी', total: '56 / 84 पद', pct: 67, icon: BookOpen, color: '#F39C12', status: '↑ 6 पद शेष', statusColor: 'text-[#2ECC71]' },
  { id: 3, name: 'श्री हित\nराधा सुधानिधि जी', total: '41 / 67 श्लोक', pct: 61, icon: Flower2, color: '#E8388B', status: '↑ 5 श्लोक शेष', statusColor: 'text-[#2ECC71]' },
  { id: 4, name: 'श्री हित\nसेवक वाणी', total: '18 / 31 छंद', pct: 58, icon: Feather, color: '#5B86E5', status: '↑ 2 छंद शेष', statusColor: 'text-[#2ECC71]' },
  { id: 5, name: 'अष्टयाम\nसेवा पद्धति', total: '5 / 8 समय', pct: 63, icon: Flame, color: '#F39C12', status: '3 समय शेष', statusColor: 'text-[#F39C12]' },
  { id: 6, name: 'नित्य पाठ\nरसोपासना', total: '22 / 31 दिन', pct: 71, icon: Landmark, color: '#00CEC9', status: '🔥 7 दिन', statusColor: 'text-[#F39C12]' },
  { id: 7, name: 'श्री वृंदावन\nशत लीला', total: '33 / 60 छंद', pct: 55, icon: Eye, color: '#9B59B6', status: '↑ 5 छंद शेष', statusColor: 'text-[#2ECC71]' },
];

const quickActions = [
  { icon: CircleDot, title: 'नाम जप जोड़ें', sub: 'राधा या हरिवंश नाम जप दर्ज करें', color: 'text-[#E8388B]' },
  { icon: BookOpen, title: 'चतुरसी पद पढ़ें', sub: 'आज के पद को पढ़ें और चिह्नित करें', color: 'text-[#F39C12]' },
  { icon: Flower2, title: 'राधा सुधानिधि पढ़ें', sub: 'श्लोक और अर्थ पढ़ें', color: 'text-[#E8388B]' },
  { icon: Feather, title: 'सेवक वाणी पढ़ें', sub: 'छंद और अर्थ पढ़ें', color: 'text-[#5B86E5]' },
  { icon: Flame, title: 'अष्टयाम सेवा पूरी करें', sub: 'सेवा समय चिह्नित करें', color: 'text-[#F39C12]' },
  { icon: Landmark, title: 'नित्य पाठ पूरा करें', sub: 'आज का पाठ पूरा करें', color: 'text-[#00CEC9]' },
];

// --- Shared Components ---
const GlassCard = ({ children, className = "", glowColor = "transparent" }) => (
  <div 
    className={`relative rounded-[20px] bg-white/[0.03] backdrop-blur-md border border-white/[0.08] overflow-hidden ${className}`}
    style={{ boxShadow: `0 4px 30px rgba(0,0,0,0.5), inset 0 0 20px ${glowColor}` }}
  >
    {children}
  </div>
);

const CircularProgress = ({ percentage, color, size = 60, strokeWidth = 4, children }) => {
  const radius = (size - strokeWidth) / 2;
  const circumference = radius * 2 * Math.PI;
  const offset = circumference - (percentage / 100) * circumference;

  return (
    <div className="relative flex items-center justify-center" style={{ width: size, height: size }}>
      <svg className="transform -rotate-90 w-full h-full">
        <circle cx={size/2} cy={size/2} r={radius} stroke="rgba(255,255,255,0.1)" strokeWidth={strokeWidth} fill="none" />
        <circle 
          cx={size/2} cy={size/2} r={radius} 
          stroke={color} strokeWidth={strokeWidth} fill="none" 
          strokeDasharray={circumference} strokeDashoffset={offset}
          strokeLinecap="round"
          className="transition-all duration-1000 ease-out drop-shadow-[0_0_8px_currentColor]"
        />
      </svg>
      <div className="absolute inset-0 flex flex-col items-center justify-center text-center">
        {children}
      </div>
    </div>
  );
};

export default function NaamSmaranDashboard() {
  return (
    <div className="relative w-full min-h-screen bg-[#0A0510] font-sans text-white overflow-x-hidden selection:bg-[#E8388B]/30 pb-28 pt-8 px-4 sm:px-6 flex flex-col gap-6">
      
      {/* Scrollbar CSS */}
      <style dangerouslySetInnerHTML={{__html: `
        ::-webkit-scrollbar { width: 4px; height: 4px; }
        ::-webkit-scrollbar-track { background: transparent; }
        ::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.15); border-radius: 10px; }
        ::-webkit-scrollbar-thumb:hover { background: rgba(255,255,255,0.3); }
        .text-gradient { background-clip: text; -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
      `}} />

      {/* --- SECTION 1: WEEKLY GLANCE --- */}
      <GlassCard className="p-5 flex flex-col md:flex-row justify-between items-center gap-6">
        <div className="w-full flex justify-between items-center mb-2 md:hidden">
          <h2 className="text-lg text-[#F5CBA7] font-medium font-serif">सप्ताह की झलक</h2>
          <button className="text-xs text-white/50 flex items-center gap-1 bg-white/5 px-2 py-1 rounded-full">इस सप्ताह <ChevronDown size={12} /></button>
        </div>

        {/* Left: Score */}
        <div className="flex items-center gap-4 border-b md:border-b-0 md:border-r border-white/10 pb-4 md:pb-0 md:pr-6 w-full md:w-auto">
          <div className="w-12 h-12 rounded-full bg-[#E8388B]/10 border border-[#E8388B]/20 flex items-center justify-center shadow-[0_0_15px_rgba(232,56,139,0.2)]">
            <Flower2 className="text-[#E8388B]" size={24} />
          </div>
          <div>
            <h3 className="text-2xl font-bold flex items-baseline gap-1">
              6 <span className="text-sm text-white/50 font-normal">/ 7 दिन</span>
            </h3>
            <p className="text-xs text-white/60">लक्ष्य पूरे किए</p>
            <div className="w-24 h-1 bg-white/10 rounded-full mt-2 overflow-hidden">
              <div className="h-full bg-gradient-to-r from-[#E8388B] to-[#F39C12] w-[85%]" />
            </div>
          </div>
        </div>

        {/* Right: Days Array */}
        <div className="flex-1 flex justify-between w-full">
          <div className="hidden md:block absolute top-4 right-4">
             <button className="text-xs text-white/50 flex items-center gap-1 bg-white/5 px-2 py-1 rounded-full">इस सप्ताह <ChevronDown size={12} /></button>
          </div>
          <h2 className="hidden md:block absolute top-4 left-6 text-lg text-[#F5CBA7] font-medium font-serif">सप्ताह की झलक</h2>
          
          <div className="flex justify-between w-full md:w-auto md:gap-8 md:mx-auto md:pt-4">
            {weeklyData.map((d, i) => (
              <div key={i} className="flex flex-col items-center gap-2">
                <span className="text-[11px] text-white/60">{d.day}</span>
                <div className={`w-8 h-8 rounded-full flex items-center justify-center border-2 
                  ${d.state === 'done' ? 'border-[#2ECC71] bg-[#2ECC71]/10 text-[#2ECC71]' : 
                    d.state === 'partial' ? 'border-[#E8388B] border-t-transparent text-[#E8388B]' : 
                    'border-dashed border-white/20 text-transparent'}`}
                >
                  {d.state === 'done' && <Check size={14} strokeWidth={3} />}
                </div>
                <span className="text-[10px] text-white/40">{d.date}</span>
              </div>
            ))}
          </div>
        </div>
      </GlassCard>

      {/* --- SECTION 2: CHARTS --- */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        
        {/* Chart 1: Target Trends */}
        <GlassCard className="p-5">
          <div className="flex justify-between items-center mb-6">
            <h3 className="text-lg text-[#F5CBA7] font-medium font-serif">लक्ष्य रुझान <span className="text-sm text-white/50">(पिछले 30 दिन)</span></h3>
            <button className="text-xs text-white/50 flex items-center gap-1 bg-white/5 px-2 py-1 rounded-full">नाम जप (राधा) <ChevronDown size={12} /></button>
          </div>
          
          <div className="mb-4">
            <h4 className="text-3xl font-bold tracking-tight">8,76,540</h4>
            <p className="text-xs text-white/60 mb-1">कुल नाम जप</p>
            <p className="text-xs text-[#2ECC71]">↑ 1,24,560 पिछले 30 दिनों में</p>
          </div>

          <div className="h-[160px] w-full -ml-4">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={trendData} margin={{ top: 10, right: 0, left: 0, bottom: 0 }}>
                <defs>
                  <linearGradient id="colorValue" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#E8388B" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="#E8388B" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <Tooltip 
                  contentStyle={{ backgroundColor: '#0A0510', borderColor: 'rgba(255,255,255,0.1)', borderRadius: '8px' }}
                  itemStyle={{ color: '#E8388B' }}
                />
                <Area type="monotone" dataKey="value" stroke="#E8388B" strokeWidth={3} fillOpacity={1} fill="url(#colorValue)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
          <div className="flex justify-between text-[10px] text-white/40 px-2 mt-2">
            <span>28 अप्रैल</span><span>5 मई</span><span>12 मई</span><span>19 मई</span><span>आज</span>
          </div>
        </GlassCard>

        {/* Chart 2: Target vs Achievement */}
        <GlassCard className="p-5 flex flex-col">
          <div className="flex justify-between items-center mb-6">
            <h3 className="text-lg text-[#F5CBA7] font-medium font-serif">लक्ष्य बनाम प्राप्ति</h3>
            <button className="text-xs text-white/50 flex items-center gap-1 bg-white/5 px-2 py-1 rounded-full">इस महीने <ChevronDown size={12} /></button>
          </div>

          <div className="flex-1 flex flex-row items-center justify-between px-2">
            <div className="relative w-[150px] h-[150px]">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={donutData}
                    innerRadius={55}
                    outerRadius={70}
                    paddingAngle={3}
                    dataKey="value"
                    stroke="none"
                    cornerRadius={4}
                  >
                    {donutData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                </PieChart>
              </ResponsiveContainer>
              <div className="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
                <span className="text-3xl font-bold">71%</span>
                <span className="text-[10px] text-white/50">कुल प्रगति</span>
              </div>
            </div>

            <div className="flex flex-col gap-4 flex-1 ml-8">
              {donutData.map((item, i) => (
                <div key={i} className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <div className="w-2.5 h-2.5 rounded-full shadow-[0_0_8px_currentColor]" style={{ backgroundColor: item.color, color: item.color }}></div>
                    <span className="text-sm text-white/80">{item.name}</span>
                  </div>
                  <span className="text-sm font-medium text-white/90">{item.value} दिन</span>
                </div>
              ))}
            </div>
          </div>
          <p className="text-center text-xs text-white/40 mt-4">कुल दिन: 31</p>
        </GlassCard>

      </div>

      {/* --- SECTION 3: 7 SADHANAS OVERVIEW --- */}
      <GlassCard className="p-5 pb-6">
        <h3 className="text-lg text-[#F5CBA7] font-medium font-serif mb-6">सातों साधना संक्षिप्त स्थिति</h3>
        
        <div className="flex gap-4 overflow-x-auto pb-4 no-scrollbar">
          {sadhanas.map((s) => (
            <div key={s.id} className="min-w-[130px] flex-shrink-0 bg-white/[0.02] border border-white/[0.05] rounded-2xl p-4 flex flex-col items-center justify-between hover:bg-white/[0.05] transition-colors group">
              
              <div className="flex flex-col items-center text-center mb-4">
                <div className={`w-12 h-12 rounded-full mb-2 flex items-center justify-center bg-white/[0.03] border border-white/10 group-hover:scale-110 transition-transform`} style={{ color: s.color, boxShadow: `0 0 15px ${s.color}20` }}>
                  <s.icon strokeWidth={1.5} size={24} />
                </div>
                <span className="text-[#F5CBA7] text-[10px] font-bold mb-1">{s.id}</span>
                <h4 className="text-xs font-medium whitespace-pre-line leading-tight text-white/90 h-[28px]">{s.name}</h4>
              </div>

              {s.id === 1 ? (
                // Special layout for Track 1 (Dual)
                <div className="w-full flex flex-col gap-4 items-center border-t border-white/5 pt-3">
                  <div className="flex flex-col items-center">
                    <span className="text-[10px] text-white/40">राधा नाम</span>
                    <span className="text-[11px] font-bold text-white/90">{s.total}</span>
                    <span className="text-[9px] text-white/40">{s.sub}</span>
                    <div className="mt-2"><CircularProgress percentage={s.pct} color="#E8388B" size={44} strokeWidth={3}><span className="text-[10px] text-white/80">{s.pct}%</span></CircularProgress></div>
                  </div>
                  <div className="flex flex-col items-center">
                    <span className="text-[10px] text-white/40">हरिवंश नाम</span>
                    <span className="text-[11px] font-bold text-white/90">{s.label2}</span>
                    <span className="text-[9px] text-white/40">{s.sub2}</span>
                    <div className="mt-2"><CircularProgress percentage={s.pct2} color="#5B86E5" size={44} strokeWidth={3}><span className="text-[10px] text-white/80">{s.pct2}%</span></CircularProgress></div>
                  </div>
                </div>
              ) : (
                // Standard Layout for Tracks 2-7
                <div className="flex flex-col items-center border-t border-white/5 pt-3 w-full">
                   <span className="text-sm font-bold text-white/90 mb-3">{s.total}</span>
                   <CircularProgress percentage={s.pct} color={s.color} size={54}>
                     <span className="text-[11px] text-white/80 font-bold">{s.pct}%</span>
                   </CircularProgress>
                   <span className={`text-[10px] mt-4 font-medium ${s.statusColor}`}>{s.status}</span>
                </div>
              )}
            </div>
          ))}
        </div>
      </GlassCard>

      {/* --- SECTION 4: QUICK ACTIONS --- */}
      <GlassCard className="p-5">
        <h3 className="text-lg text-[#F5CBA7] font-medium font-serif mb-6">लक्ष्य शीघ्र क्रियाएँ</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          {quickActions.map((action, i) => (
            <button key={i} className="flex items-center justify-between p-4 rounded-xl bg-white/[0.02] border border-white/[0.05] hover:bg-white/[0.06] transition-all text-left group">
              <div className="flex items-center gap-4">
                <div className={`w-10 h-10 rounded-full flex items-center justify-center bg-white/[0.03] border border-white/10 ${action.color} group-hover:scale-110 transition-transform`}>
                  <action.icon size={18} strokeWidth={2} />
                </div>
                <div>
                  <h4 className="text-sm font-medium text-white/90">{action.title}</h4>
                  <p className="text-[11px] text-white/40 mt-0.5">{action.sub}</p>
                </div>
              </div>
              <ChevronRight size={16} className="text-white/30 group-hover:text-white/70 transition-colors" />
            </button>
          ))}
        </div>
      </GlassCard>

      {/* --- SECTION 5: INSPIRATIONAL VERSE --- */}
      <GlassCard className="overflow-hidden p-0 relative border-white/10" glowColor="rgba(232,56,139,0.1)">
        <div 
          className="absolute inset-0 z-0 opacity-60 mix-blend-screen"
          style={{
            backgroundImage: `url('https://images.unsplash.com/photo-1590074061805-492790924976?q=80&w=2070&auto=format&fit=crop')`,
            backgroundSize: 'cover',
            backgroundPosition: 'center 40%',
          }}
        />
        <div className="absolute inset-0 z-0 bg-gradient-to-r from-[#0A0510] via-[#0A0510]/80 to-transparent pointer-events-none" />
        
        <div className="relative z-10 p-6 flex flex-col md:flex-row items-center md:items-start justify-between gap-6">
          <div className="max-w-md">
            <h3 className="text-lg text-[#F5CBA7] font-medium font-serif mb-4 flex items-center gap-2">
              <Flower2 size={16} className="text-[#E8388B]" /> आज का प्रेरक पद
            </h3>
            <div className="pl-4 border-l-2 border-[#E8388B]/50 font-serif leading-relaxed text-[15px]">
              <p className="text-white/90 mb-1">काहू के बल भजन काहू के बल आचार,</p>
              <p className="text-white/90 mb-1">व्यास भरोसे कुवरी के सोवत पाँव पसार —</p>
              <p className="text-[#E8388B] font-bold mt-2">हमारे माई श्याम जू को राज ॥</p>
            </div>
            <p className="text-xs text-white/50 mt-4 pl-4">- श्री हित चतुरसी जी</p>
          </div>
          {/* Decorative floating peacock feather could go here as an SVG, using an emoji for lightness */}
          <div className="hidden md:block text-5xl opacity-40 filter drop-shadow-[0_0_10px_#B8C8FF]">🦚</div>
        </div>
      </GlassCard>

      {/* --- FIXED BOTTOM NAVIGATION --- */}
      <div className="fixed bottom-0 left-0 w-full h-[80px] bg-[#0A0510]/90 backdrop-blur-2xl border-t border-white/[0.08] flex justify-between items-center px-6 z-50">
        <button className="flex flex-col items-center gap-1 text-[#E8388B]">
          <Home size={22} />
          <span className="text-[10px] font-medium">Dashboard</span>
        </button>
        <button className="flex flex-col items-center gap-1 text-white/40 hover:text-white/80 transition-colors">
          <BarChart2 size={22} />
          <span className="text-[10px] font-medium">Analytics</span>
        </button>
        
        {/* Central Floating Action Button */}
        <div className="relative -top-6">
          <div className="absolute inset-0 bg-[#E8388B] blur-xl opacity-40 rounded-full animate-pulse"></div>
          <button className="relative w-16 h-16 bg-gradient-to-tr from-[#E8388B] to-[#F5D76E] rounded-full flex items-center justify-center shadow-[0_8px_30px_rgba(232,56,139,0.4)] border border-white/20 text-white hover:scale-105 transition-transform">
            <Plus size={32} strokeWidth={2.5} />
          </button>
        </div>

        <button className="flex flex-col items-center gap-1 text-white/40 hover:text-white/80 transition-colors">
          <Target size={22} />
          <span className="text-[10px] font-medium">Goals</span>
        </button>
        <button className="flex flex-col items-center gap-1 text-white/40 hover:text-white/80 transition-colors">
          <User size={22} />
          <span className="text-[10px] font-medium">Profile</span>
        </button>
      </div>

    </div>
  );
}