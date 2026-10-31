package com.qualityhub.repository;

import com.qualityhub.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


@Repository
public class TaskRepository {

    private final JdbcTemplate jdbc;

    public TaskRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Task> mapper = (rs, rowNum) -> new Task(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("priority"),
            rs.getString("status"),
            rs.getTimestamp("created_at").toLocalDateTime());

    public List<Task> findAll(String search, String status) {
        return jdbc.query("""
                SELECT * FROM tasks
                WHERE (? = '' OR LOWER(title) LIKE LOWER(?))
                  AND (? = '' OR status = ?)
                ORDER BY id DESC
                """,
                mapper,
                search, "%" + search + "%",
                status, status);
    }

    public Optional<Task> findById(long id) {
        return jdbc.query(
                "SELECT * FROM tasks WHERE id = ?",
                mapper,
                id).stream().findFirst();
    }

    public Task create(String title, String description, String priority) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    """
                            INSERT INTO tasks (title, description, priority, status)
                            VALUES (?, ?, ?, 'TODO')
                            """,
                    new String[] { "id" });

            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, priority);

            return statement;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();

        if (generatedId == null) {
            throw new IllegalStateException("Task ID was not generated");
        }

        return findById(generatedId.longValue())
                .orElseThrow(() -> new IllegalStateException("Task not found after creation"));
    }

    public Optional<Task> updateStatus(long id, String status) {
        int updated = jdbc.update(
                "UPDATE tasks SET status = ? WHERE id = ?",
                status, id);

        return updated == 0 ? Optional.empty() : findById(id);
    }

    public boolean delete(long id) {
        return jdbc.update(
                "DELETE FROM tasks WHERE id = ?", id) > 0;
    }
}
