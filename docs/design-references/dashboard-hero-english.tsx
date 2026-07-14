import React from 'react';
import { 
  Bell, Crown, Flame, BookOpen, Target, 
  Plus, Home as HomeIcon, BarChart2, User,
  ChevronDown, ChevronRight, Activity
} from 'lucide-react';
import { 
  PieChart, Pie, Cell, ResponsiveContainer, 
  LineChart, Line, BarChart, Bar
} from 'recharts';

// --- Mock Data ---
const donutData = [
  { name: 'नाम जप', value: 350, color: '#E8388B' }, // Pink
  { name: 'ग्रंथ पाठ', value: 250, color: '#F39C12' }, // Orange
  { name: 'अष्टयाम सेवा', value: 150, color: '#00CEC9' }, // Teal
  { name: 'उपासना', value: 100, color: '#2ECC71' }, // Green
  { name: 'अन्य सेवा', value: 150, color: '#9B59B6' }, // Purple
];

const activityData = [
  { id: 1, title: 'नाम जप', date: 'May 20, 2024', value: '108 Beads', icon: '📿', color: 'text-[#E8388B]', bg: 'bg-[#E8388B]/10' },
  { id: 2, title: 'श्री हित चतुरसी जी', date: 'May 20, 2024', value: '1 Pad', icon: '📖', color: 'text-[#F39C12]', bg: 'bg-[#F39C12]/10' },
  { id: 3, title: 'अष्टयाम सेवा', date: 'May 19, 2024', value: '2 Hours', icon: '🏛️', color: 'text-[#00CEC9]', bg: 'bg-[#00CEC9]/10' },
  { id: 4, title: 'नित्य पाठ रसोपासना', date: 'May 19, 2024', value: '11 Hours', icon: '🌸', color: 'text-[#2ECC71]', bg: 'bg-[#2ECC71]/10' },
  { id: 5, title: 'श्री वृंदावन शत लीला', date: 'May 19, 2024', value: '1 Pad', icon: '📚', color: 'text-[#9B59B6]', bg: 'bg-[#9B59B6]/10' },
];

const sparklineData = Array.from({ length: 10 }).map(() => ({ value: Math.random() * 100 }));
const barData1 = Array.from({ length: 12 }).map(() => ({ value: Math.random() * 100 }));
const barData2 = Array.from({ length: 12 }).map(() => ({ value: Math.random() * 100 }));
const barData3 = Array.from({ length: 12 }).map(() => ({ value: Math.random() * 100 }));

const GlassCard = ({ children, className = "", glowColor = "rgba(255,255,255,0.05)" }) => (
  <div 
    className={`relative rounded-2xl bg-white/[0.03] backdrop-blur-xl border border-white/10 overflow-hidden ${className}`}
    style={{ boxShadow: `0 4px 30px ${glowColor}, inset 0 0 20px ${glowColor}` }}
  >
    {children}
  </div>
);

const IconButton = ({ icon: Icon, className = "" }) => (
  <button className={`w-10 h-10 rounded-full bg-white/[0.05] border border-white/10 flex items-center justify-center hover:bg-white/10 transition-colors ${className}`}>
    <Icon size={18} className="text-white" />
  </button>
);

export default function NaamSmaranDashboard() {
  return (
    <div className="relative w-full min-h-screen bg-[#090514] font-sans text-white overflow-x-hidden selection:bg-[#E8388B]/30 pb-24">
      
      {/* Global CSS for custom scrollbar to match premium feel */}
      <style dangerouslySetInnerHTML={{__html: `
        ::-webkit-scrollbar { width: 6px; }
        ::-webkit-scrollbar-track { background: transparent; }
        ::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 10px; }
        ::-webkit-scrollbar-thumb:hover { background: rgba(255,255,255,0.2); }
      `}} />

      {/* --- HERO HEADER --- */}
      <div className="relative w-full h-[340px] px-6 pt-12 pb-6 flex flex-col justify-between">
        {/* Immersive Background Image with Heavy Gradients */}
        <div 
          className="absolute inset-0 z-0 opacity-80"
          style={{
            backgroundImage: `url('https://images.unsplash.com/photo-1590074061805-492790924976?q=80&w=2070&auto=format&fit=crop')`, // Placeholder for the moonlit temple
            backgroundSize: 'cover',
            backgroundPosition: 'center 20%',
          }}
        />
        <div className="absolute inset-0 z-0 bg-gradient-to-b from-[#090514]/40 via-[#090514]/60 to-[#090514] pointer-events-none" />
        <div className="absolute inset-0 z-0 bg-gradient-to-r from-[#090514] via-transparent to-transparent pointer-events-none" />

        {/* Top Bar */}
        <div className="relative z-10 flex justify-between items-center">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 rounded-full border-2 border-[#E8388B]/50 p-0.5 overflow-hidden shadow-[0_0_15px_rgba(232,56,139,0.3)]">
              <img src="https://i.pravatar.cc/150?img=11" alt="Profile" className="w-full h-full rounded-full object-cover" />
            </div>
            <div>
              <p className="text-sm text-white/70 font-light">Good Morning,</p>
              <h1 className="text-xl font-bold tracking-wide flex items-center gap-2">
                Alex <span className="text-2xl animate-wave origin-bottom-right">👋</span>
              </h1>
            </div>
          </div>
          <IconButton icon={Bell} className="relative shadow-[0_0_15px_rgba(255,255,255,0.1)]">
            <span className="absolute top-2 right-2 w-2 h-2 bg-[#E8388B] rounded-full"></span>
          </IconButton>
        </div>

        {/* Hero Text */}
        <div className="relative z-10 mt-8 max-w-[280px]">
          <div className="flex items-center gap-2 text-[#F5CBA7] mb-2 text-sm">
            <span>✻</span>
            <span className="font-serif tracking-widest">राधा</span>
            <span>✻</span>
          </div>
          <h2 className="text-4xl font-light leading-tight mb-2">
            Stay <span className="font-medium text-transparent bg-clip-text bg-gradient-to-r from-[#E8388B] to-[#B084FF]">focused</span>,<br/>
            achieve your goals
          </h2>
          <p className="text-white/60 text-sm">You're doing great today! <Flame size={16} className="inline text-orange-500 pb-0.5" /></p>
        </div>
      </div>

      <div className="relative z-20 px-4 space-y-6 -mt-4">
        
        {/* --- OVERVIEW STRIP --- */}
        <div>
          <div className="flex justify-between items-center mb-3 px-2">
            <h3 className="text-lg font-medium text-white/90">Overview</h3>
            <button className="text-xs text-white/50 flex items-center gap-1 bg-white/5 px-2 py-1 rounded-full">
              This Month <ChevronDown size={12} />
            </button>
          </div>
          
          {/* Scrollable Row for Cards */}
          <div className="flex gap-4 overflow-x-auto pb-4 no-scrollbar px-2">
            {/* Card 1: Targets */}
            <GlassCard className="min-w-[150px] p-4 flex-shrink-0" glowColor="rgba(232,56,139,0.08)">
              <div className="flex items-center gap-2 mb-3">
                <div className="w-8 h-8 rounded-full bg-[#E8388B]/20 flex items-center justify-center text-[#E8388B]">
                  <Crown size={16} />
                </div>
                <span className="text-xs text-white/70 leading-tight">Overall Targets<br/>Completed</span>
              </div>
              <h4 className="text-2xl font-bold mb-2">25</h4>
              <div className="h-[40px] w-full">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart data={sparklineData}>
                    <Line type="monotone" dataKey="value" stroke="#E8388B" strokeWidth={2} dot={false} />
                  </LineChart>
                </ResponsiveContainer>
              </div>
            </GlassCard>

            {/* Card 2: Naam Japped */}
            <GlassCard className="min-w-[150px] p-4 flex-shrink-0" glowColor="rgba(46,204,113,0.08)">
              <div className="flex items-center gap-2 mb-3">
                <div className="w-8 h-8 rounded-full bg-[#2ECC71]/20 flex items-center justify-center text-[#2ECC71]">
                  <Activity size={16} />
                </div>
                <span className="text-xs text-white/70 leading-tight">Naam Japped<br/>(Beads)</span>
              </div>
              <h4 className="text-2xl font-bold mb-1">25,000</h4>
              <p className="text-xs text-[#2ECC71] flex items-center gap-1 mb-2">↑ 12.5%</p>
              <div className="h-[30px] w-full">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={barData1}>
                    <Bar dataKey="value" fill="#2ECC71" radius={[2,2,0,0]} opacity={0.7} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </GlassCard>

            {/* Card 3: Seva */}
            <GlassCard className="min-w-[150px] p-4 flex-shrink-0" glowColor="rgba(243,156,18,0.08)">
              <div className="flex items-center gap-2 mb-3">
                <div className="w-8 h-8 rounded-full bg-[#F39C12]/20 flex items-center justify-center text-[#F39C12]">
                  <HomeIcon size={16} />
                </div>
                <span className="text-xs text-white/70 leading-tight">Seva Performed<br/>(Hours)</span>
              </div>
              <h4 className="text-2xl font-bold mb-1">40</h4>
              <div className="h-[30px] w-full mt-6">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={barData2}>
                    <Bar dataKey="value" fill="#F39C12" radius={[2,2,0,0]} opacity={0.4} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </GlassCard>

            {/* Card 4: Texts */}
            <GlassCard className="min-w-[150px] p-4 flex-shrink-0" glowColor="rgba(0,206,201,0.08)">
              <div className="flex items-center gap-2 mb-3">
                <div className="w-8 h-8 rounded-full bg-[#00CEC9]/20 flex items-center justify-center text-[#00CEC9]">
                  <BookOpen size={16} />
                </div>
                <span className="text-xs text-white/70 leading-tight">Texts Completed<br/>(Chapters)</span>
              </div>
              <h4 className="text-2xl font-bold mb-1">12</h4>
              <p className="text-xs text-[#00CEC9] flex items-center gap-1 mb-2">↑ 16.3%</p>
              <div className="h-[30px] w-full">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={barData3}>
                    <Bar dataKey="value" fill="#00CEC9" radius={[2,2,0,0]} opacity={0.7} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </GlassCard>
          </div>
        </div>

        {/* --- MAIN DASHBOARD GRID --- */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          
          {/* Seva & Sadhana Overview (Donut Chart) */}
          <GlassCard className="p-5">
            <div className="flex justify-between items-center mb-4">
              <div>
                <h3 className="text-lg font-medium text-white/90">Seva & Sadhana Overview</h3>
                <button className="text-xs text-[#F5CBA7] mt-1 flex items-center gap-1">
                  <span className="text-[10px]">✻</span> This Month <ChevronDown size={12} />
                </button>
              </div>
              <div className="flex gap-1">
                <div className="w-1.5 h-1.5 rounded-full bg-white/40"></div>
                <div className="w-1.5 h-1.5 rounded-full bg-white/40"></div>
                <div className="w-1.5 h-1.5 rounded-full bg-white/40"></div>
              </div>
            </div>

            <div className="flex flex-row items-center justify-between">
              {/* Chart */}
              <div className="relative w-[140px] h-[140px]">
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={donutData}
                      innerRadius={45}
                      outerRadius={65}
                      paddingAngle={2}
                      dataKey="value"
                      stroke="none"
                    >
                      {donutData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={entry.color} />
                      ))}
                    </Pie>
                  </PieChart>
                </ResponsiveContainer>
                {/* Center Label */}
                <div className="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
                  <span className="text-xl font-bold">1000</span>
                  <span className="text-[10px] text-white/50">Total</span>
                </div>
              </div>

              {/* Legend */}
              <div className="flex flex-col gap-3 flex-1 ml-6">
                {donutData.map((item, i) => (
                  <div key={i} className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <div className="w-6 h-6 rounded-full flex items-center justify-center" style={{ backgroundColor: `${item.color}20` }}>
                        <div className="w-2 h-2 rounded-full" style={{ backgroundColor: item.color }}></div>
                      </div>
                      <div>
                        <p className="text-[11px] text-white/90 leading-tight font-serif">{item.name}</p>
                        <p className="text-[10px] text-white/40">{item.value} Units</p>
                      </div>
                    </div>
                    <span className="text-xs text-white/60">{Math.round((item.value/1000)*100)}%</span>
                  </div>
                ))}
              </div>
            </div>

            <div className="mt-6 pt-4 border-t border-white/10 text-center">
              <p className="text-xs text-[#F5CBA7]/80 font-serif tracking-wide flex items-center justify-center gap-2">
                <span>✻</span> सेवा में ही सिद्धि है, सेवा में ही आनंद है । <span>🌸</span>
              </p>
            </div>
          </GlassCard>

          {/* Recent Activity */}
          <GlassCard className="p-5">
            <div className="flex justify-between items-center mb-6">
              <h3 className="text-lg font-medium text-white/90">Recent Activity</h3>
              <button className="text-sm text-[#E8388B] hover:text-[#FF6B9E]">See All</button>
            </div>

            <div className="flex flex-col gap-4">
              {activityData.map((item) => (
                <div key={item.id} className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className={`w-10 h-10 rounded-full flex items-center justify-center text-lg ${item.bg}`}>
                      {item.icon}
                    </div>
                    <div>
                      <h4 className="text-sm font-medium text-white/90 font-serif">{item.title}</h4>
                      <p className="text-[11px] text-white/40">{item.date}</p>
                    </div>
                  </div>
                  <span className={`text-sm font-medium ${item.color}`}>{item.value}</span>
                </div>
              ))}
            </div>
          </GlassCard>

        </div>

        {/* --- GOALS SECTION --- */}
        <GlassCard className="p-5 flex items-center justify-between mb-8" glowColor="rgba(232,56,139,0.15)">
          <div className="flex items-center gap-4 flex-1">
            <div className="w-12 h-12 rounded-full bg-[#E8388B]/20 border border-[#E8388B]/30 flex items-center justify-center text-[#E8388B] shadow-[0_0_15px_rgba(232,56,139,0.3)]">
              <Target size={24} />
            </div>
            <div className="flex-1 max-w-[200px]">
              <h4 className="text-sm font-medium text-white/90 font-serif mb-1">राधा नाम जप लक्ष्य</h4>
              <p className="text-xs text-white/50 mb-2">25,000 of 35,000</p>
              <div className="w-full h-1.5 bg-white/10 rounded-full overflow-hidden">
                <div className="h-full bg-gradient-to-r from-[#E8388B] to-[#B084FF]" style={{ width: '71%' }}></div>
              </div>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <span className="text-xs text-white/40">71%</span>
            <div className="text-3xl filter drop-shadow-[0_0_10px_rgba(232,56,139,0.6)]">🌸</div>
            <IconButton icon={ChevronRight} className="border-none bg-transparent hover:bg-white/5 w-8 h-8" />
          </div>
        </GlassCard>

      </div>

      {/* --- FIXED BOTTOM NAVIGATION --- */}
      <div className="fixed bottom-0 left-0 w-full h-[80px] bg-[#090514]/90 backdrop-blur-2xl border-t border-white/10 flex justify-between items-center px-6 z-50">
        <button className="flex flex-col items-center gap-1 text-[#E8388B]">
          <HomeIcon size={22} />
          <span className="text-[10px] font-medium">Dashboard</span>
        </button>
        <button className="flex flex-col items-center gap-1 text-white/40 hover:text-white/80 transition-colors">
          <BarChart2 size={22} />
          <span className="text-[10px] font-medium">Analytics</span>
        </button>
        
        {/* Central Floating Action Button */}
        <div className="relative -top-6">
          <div className="absolute inset-0 bg-[#E8388B] blur-xl opacity-40 rounded-full"></div>
          <button className="relative w-16 h-16 bg-gradient-to-tr from-[#E8388B] to-[#B084FF] rounded-full flex items-center justify-center shadow-[0_8px_30px_rgba(232,56,139,0.4)] border-2 border-[#090514] text-white hover:scale-105 transition-transform">
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