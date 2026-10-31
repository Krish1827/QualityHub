
import { useCallback, useEffect, useMemo, useState } from "react";
import {
  ClipboardList,
  Clock3,
  CheckCircle2,
  Plus,
  Search,
  RefreshCw,
} from "lucide-react";

import Sidebar from "./components/Sidebar";
import StatCard from "./components/Statcard";
import TaskTable from "./components/TaskTable";
import TaskModal from "./components/TaskModal";

import {
  getTasks,
  createTask,
  updateTaskStatus,
  deleteTask,
} from "./services/taskService";

import "./App.css";

export default function App() {
  const [tasks, setTasks] = useState([]);
  const [active, setActive] = useState("Overview");
  const [search, setSearch] = useState("");
  const [status, setStatus] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadTasks = useCallback(async () => {
    setLoading(true);
    setError("");

    try {
      const data = await getTasks(search, status);
      setTasks(data);
    } catch (err) {
      setError(err.message || "Could not connect to the backend.");
    } finally {
      setLoading(false);
    }
  }, [search, status]);

  useEffect(() => {
    loadTasks();
  }, [loadTasks]);

  const stats = useMemo(() => ({
    total: tasks.length,
    todo: tasks.filter((task) => task.status === "TODO").length,
    progress: tasks.filter((task) => task.status === "IN_PROGRESS").length,
    done: tasks.filter((task) => task.status === "DONE").length,
  }), [tasks]);

  const visibleTasks = active === "Completed"
    ? tasks.filter((task) => task.status === "DONE")
    : tasks;

  async function handleCreate(task) {
    await createTask(task);
    await loadTasks();
  }

  async function handleStatusChange(id, newStatus) {
    try {
      await updateTaskStatus(id, newStatus);
      await loadTasks();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleDelete(id) {
    if (!window.confirm("Are you sure you want to delete this task?")) {
      return;
    }

    try {
      await deleteTask(id);
      await loadTasks();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div className="app-layout">
      <Sidebar active={active} onChange={setActive} />

      <main className="main-content">
        <header className="topbar">
          <div>
            <span className="breadcrumb">Workspace / {active}</span>
            <h1>{active}</h1>
          </div>

          <button className="primary-button" onClick={() => setShowModal(true)}>
            <Plus size={18} />
            New task
          </button>
        </header>

        <section className="welcome">
          <div>
            <h2>Welcome to QualityHub</h2>
            <p>Manage your work and keep track of task progress.</p>
          </div>
          <button className="refresh-button" onClick={loadTasks}>
            <RefreshCw size={16} /> Refresh
          </button>
        </section>

        <section className="stats-grid">
          <StatCard title="Total Tasks" value={stats.total}
            icon={ClipboardList} color="blue" subtitle="Matching tasks" />
          <StatCard title="To Do" value={stats.todo}
            icon={Clock3} color="orange" subtitle="Waiting to start" />
          <StatCard title="In Progress" value={stats.progress}
            icon={RefreshCw} color="purple" subtitle="Currently active" />
          <StatCard title="Completed" value={stats.done}
            icon={CheckCircle2} color="green" subtitle="Successfully finished" />
        </section>

        <section className="tasks-section">
          <div className="section-heading">
            <div>
              <h2>Task Management</h2>
              <p>Manage and track your workspace tasks.</p>
            </div>
          </div>

          <div className="toolbar">
            <div className="search-box">
              <Search size={18} />
              <input
                placeholder="Search tasks..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>

            <select
              value={status}
              onChange={(e) => setStatus(e.target.value)}
              aria-label="Filter tasks by status"
            >
              <option value="">All statuses</option>
              <option value="TODO">To Do</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="DONE">Completed</option>
            </select>
          </div>

          {error && <div className="error-banner">{error}</div>}

          {loading ? (
            <div className="empty-state">Loading tasks...</div>
          ) : (
            <TaskTable
              tasks={visibleTasks}
              onStatusChange={handleStatusChange}
              onDelete={handleDelete}
            />
          )}
        </section>
      </main>

      {showModal && (
        <TaskModal
          onClose={() => setShowModal(false)}
          onCreate={handleCreate}
        />
      )}
    </div>
  );
}
