
import {
  LayoutDashboard,
  ClipboardList,
  CheckCircle2,
  Settings,
  ShieldCheck,
} from "lucide-react";

export default function Sidebar({ active, onChange }) {
  const items = [
    { name: "Overview", icon: LayoutDashboard },
    { name: "All Tasks", icon: ClipboardList },
    { name: "Completed", icon: CheckCircle2 },
  ];

  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="brand-icon">
          <ShieldCheck size={23} />
        </div>
        <div>
          <h2>QualityHub</h2>
          <span>QA Management</span>
        </div>
      </div>

      <p className="nav-label">WORKSPACE</p>

      <nav>
        {items.map(({ name, icon: Icon }) => (
          <button
            key={name}
            className={`nav-item ${active === name ? "active" : ""}`}
            onClick={() => onChange(name)}
          >
            <Icon size={19} />
            <span>{name}</span>
          </button>
        ))}
      </nav>

      <div className="sidebar-bottom">
        <button className="nav-item">
          <Settings size={19} />
          <span>Settings</span>
        </button>
        <div className="profile">
          <div className="avatar">QA</div>
          <div>
            <strong>QA Workspace</strong>
            <span>Administrator</span>
          </div>
        </div>
      </div>
    </aside>
  );
}
