
import { Trash2, RefreshCw } from "lucide-react";

export default function TaskTable({
  tasks,
  onStatusChange,
  onDelete,
}) {
  if (tasks.length === 0) {
    return (
      <div className="empty-state">
        <h3>No tasks found</h3>
        <p>Create a task or change your filters.</p>
      </div>
    );
  }

  return (
    <div className="table-wrapper">
      <table>
        <thead>
          <tr>
            <th>TASK</th>
            <th>PRIORITY</th>
            <th>STATUS</th>
            <th>CREATED</th>
            <th>ACTIONS</th>
          </tr>
        </thead>

        <tbody>
          {tasks.map((task) => (
            <tr key={task.id}>
              <td>
                <strong>{task.title}</strong>
                <p className="task-description">
                  {task.description || "No description"}
                </p>
              </td>

              <td>
                <span className={`priority ${task.priority.toLowerCase()}`}>
                  {task.priority}
                </span>
              </td>

              <td>
                <select
                  className={`status-select ${task.status.toLowerCase()}`}
                  value={task.status}
                  onChange={(e) =>
                    onStatusChange(task.id, e.target.value)
                  }
                  aria-label={`Status for ${task.title}`}
                >
                  <option value="TODO">To Do</option>
                  <option value="IN_PROGRESS">In Progress</option>
                  <option value="DONE">Done</option>
                </select>
              </td>

              <td>
                {new Date(task.createdAt).toLocaleDateString()}
              </td>

              <td>
                <button
                  className="icon-button danger"
                  aria-label={`Delete ${task.title}`}
                  onClick={() => onDelete(task.id)}
                >
                  <Trash2 size={17} />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
