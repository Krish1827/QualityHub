
const API_URL = "http://localhost:8080/api/tasks";

export async function getTasks(search = "", status = "") {
  const params = new URLSearchParams({ search, status });

  const response = await fetch(`${API_URL}?${params}`);

  if (!response.ok) {
    throw new Error("Unable to fetch tasks");
  }

  return response.json();
}

export async function createTask(task) {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(task),
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || "Unable to create task");
  }

  return response.json();
}

export async function updateTaskStatus(id, status) {
  const response = await fetch(`${API_URL}/${id}/status`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ status }),
  });

  if (!response.ok) {
    throw new Error("Unable to update task");
  }

  return response.json();
}

export async function deleteTask(id) {
  const response = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
  });

  if (!response.ok) {
    throw new Error("Unable to delete task");
  }
}
