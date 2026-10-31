
export default function StatCard({ title, value, icon: Icon, color, subtitle }) {
  return (
    <div className="stat-card">
      <div className="stat-top">
        <span>{title}</span>
        <div className={`stat-icon ${color}`}>
          <Icon size={21} />
        </div>
      </div>

      <h2>{value}</h2>
      <p>{subtitle}</p>
    </div>
  );
}
