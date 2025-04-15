import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskService {
    Scanner sc = new Scanner(System.in);

    public void addTask(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.print("Title: ");
            String title = sc.nextLine();
            System.out.print("Description: ");
            String description = sc.nextLine();
            System.out.print("Priority (Low/Medium/High): ");
            String priority = sc.nextLine();
            System.out.print("Due Date (YYYY-MM-DD): ");
            String dueDate = sc.nextLine();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO tasks (user_id, title, description, priority, due_date, status) VALUES (?, ?, ?, ?, ?, 'Pending')");
            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, priority);
            ps.setDate(5, Date.valueOf(dueDate));
            ps.executeUpdate();
            System.out.println("Task added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTasks(int userId) {
        List<Task> taskList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM tasks WHERE user_id = ? ORDER BY due_date");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("priority"),
                        rs.getDate("due_date"),
                        rs.getString("status")
                );
                taskList.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    public void viewTasks(int userId) {
        List<Task> tasks = getTasks(userId);
        System.out.println("\n--- Your Tasks ---");
        for (Task t : tasks) {
            System.out.printf("ID: %d | Title: %s | Due: %s | Priority: %s | Status: %s\n",
                    t.getId(), t.getTitle(), t.getDueDate(), t.getPriority(), t.getStatus());
        }
        System.out.println("------------------\n");
    }

    public void completeTask(int userId) {
        System.out.print("Enter task ID to mark as completed: ");
        int taskId = Integer.parseInt(sc.nextLine());
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE tasks SET status = 'Completed' WHERE id = ? AND user_id = ?");
            ps.setInt(1, taskId);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Task marked as completed." : "Task not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upcomingTasks(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM tasks WHERE user_id = ? AND due_date <= ? AND status = 'Pending' ORDER BY due_date");
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(LocalDate.now().plusDays(2)));
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Upcoming Tasks (Next 2 Days) ---");
            while (rs.next()) {
                Task t = new Task(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("priority"),
                        rs.getDate("due_date"),
                        rs.getString("status")
                );
                System.out.printf("ID: %d | Title: %s | Due: %s | Priority: %s\n",
                        t.getId(), t.getTitle(), t.getDueDate(), t.getPriority());
            }
            System.out.println("------------------------------------\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProductivity(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement psTotal = conn.prepareStatement(
                    "SELECT COUNT(*) AS total FROM tasks WHERE user_id = ? AND due_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)");
            psTotal.setInt(1, userId);
            ResultSet rsTotal = psTotal.executeQuery();

            PreparedStatement psDone = conn.prepareStatement(
                    "SELECT COUNT(*) AS done FROM tasks WHERE user_id = ? AND status = 'Completed' AND due_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)");
            psDone.setInt(1, userId);
            ResultSet rsDone = psDone.executeQuery();

            if (rsTotal.next() && rsDone.next()) {
                int total = rsTotal.getInt("total");
                int done = rsDone.getInt("done");
                double percentage = total == 0 ? 0 : (done * 100.0 / total);
                System.out.printf("You completed %d of %d tasks in the last 7 days (%.2f%%)\n", done, total, percentage);
                System.out.println(percentage >= 80 ? "🔥 Great job! You had a productive week!" : "💡 Try to complete more tasks next week!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
