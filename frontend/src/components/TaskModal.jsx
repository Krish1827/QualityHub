
import { useState } from "react";
import { X } from "lucide-react";

export default function TaskModal({ onClose, onCreate }) {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState("MEDIUM");
  const [error, setError] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();

    if (!title.trim()) {
      setError("Task title is required.");
      return;
    }

    try {
      await onCreate({
        title: title.trim(),
        description,
        priority,
      });
      onClose();
    } catch (err) {
      setError(err.message || "Unable to create task.");
    }
  }

  return (
    <div className="modal-overlay" onMouseDown={(e) => {
      if (e.target === e.currentTarget) onClose();
    }}>
      <div className="modal">
        <div className="modal-header">
          <div>
            <h2>Create new task</h2>
            <p>Add a task to your workspace.</p>
          </div>
          <button className="icon-button" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <label>Task title</label>
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="e.g. Test user login"
            maxLength={120}
            autoFocus
          />

          <label>Description</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Describe the task..."
            maxLength={1000}
          />

          <label>Priority</label>
          <select
            value={priority}
            onChange={(e) => setPriority(e.target.value)}
          >
            <option value="LOW">Low</option>
            <option value="MEDIUM">Medium</option>
            <option value="HIGH">High</option>
          </select>

          {error && <p className="form-error">{error}</p>}

          <div className="modal-actions">
            <button type="button" className="secondary-button" onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className="primary-button">
              Create task
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
