import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        TaskService taskService = new TaskService();

        System.out.println("Welcome to Smart Daily Planner!");
        System.out.print("1. Register\n2. Login\nChoose option: ");
        int choice = Integer.parseInt(sc.nextLine());
        int userId = -1;

        if (choice == 1) {
            userService.register();
            userId = userService.login();
        } else if (choice == 2) {
            userId = userService.login();
        }

        if (userId != -1) {
            while (true) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Add Task");
                System.out.println("2. View All Tasks");
                System.out.println("3. Mark Task as Completed");
                System.out.println("4. Upcoming Tasks");
                System.out.println("5. Productivity Report");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                int opt = Integer.parseInt(sc.nextLine());

                switch (opt) {
                    case 1:
                        taskService.addTask(userId);
                        break;
                    case 2:
                        taskService.viewTasks(userId);
                        break;
                    case 3:
                        taskService.completeTask(userId);
                        break;
                    case 4:
                        taskService.upcomingTasks(userId);
                        break;
                    case 5:
                        taskService.showProductivity(userId);
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        System.exit(0);
                }
            }
        }
    }
}