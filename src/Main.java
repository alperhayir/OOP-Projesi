import model.*;
import service.ProjectService;
import service.TaskService;
import service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println("====================================");
            System.out.println("   GÖREV & PROJE YÖNETİM SİSTEMİ");
            System.out.println("====================================");
            System.out.println("1 - Görev Oluştur");
            System.out.println("2 - Süreli Görev Oluştur");
            System.out.println("3 - Görev Tamamla");
            System.out.println("4 - Görevi Kullanıcıya Ata");
            System.out.println("5 - Görevi Projeye Ata");
            System.out.println("6 - Yaklaşan Görevleri Listele");
            System.out.println("7 - Tüm Görevleri Listele");
            System.out.println("8 - Kullanıcı Ekle");
            System.out.println("9 - Proje Ekle");
            System.out.println("0 - Çıkış");
            System.out.print("Seçiminiz: ");

            int secim = scanner.nextInt();
            scanner.nextLine();

            switch (secim) {

                case 4 -> { // Görevi kullanıcıya ata

                    if (taskService.getAllTasks().isEmpty()) {
                        System.out.println("✖ Görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    if (userService.getAllUsers().isEmpty()) {
                        System.out.println("✖ Kullanıcı yok. Önce kullanıcı ekleyin.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    User user;
                    while (true) {
                        printAllUsers(userService);
                        System.out.print("Kullanıcı ID (0: Ana Menü): ");
                        String uid = scanner.nextLine();

                        if (uid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        user = userService.findUserById(uid);
                        if (user == null) {
                            System.out.println("✖ Kullanıcı mevcut değil, tekrar deneyin.");
                        } else break;
                    }

                    Task task;
                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Görev ID (0: Ana Menü): ");
                        String tid = scanner.nextLine();

                        if (tid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        task = taskService.findTaskById(tid);
                        if (task == null) {
                            System.out.println("✖ Görev mevcut değil, tekrar deneyin.");
                        } else break;
                    }

                    try {
                        taskService.assignTaskToUser(task, user);
                        System.out.println("✔ Görev kullanıcıya atandı.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 5 -> { // Görevi projeye ata

                    if (taskService.getAllTasks().isEmpty()) {
                        System.out.println("✖ Görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    if (projectService.getAllProjects().isEmpty()) {
                        System.out.println("✖ Proje yok. Önce proje ekleyin.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    Project project;
                    while (true) {
                        printAllProjects(projectService);
                        System.out.print("Proje ID (0: Ana Menü): ");
                        String pid = scanner.nextLine();

                        if (pid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        project = projectService.findProjectById(pid);
                        if (project == null) {
                            System.out.println("✖ Proje mevcut değil, tekrar deneyin.");
                        } else break;
                    }

                    Task task;
                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Görev ID (0: Ana Menü): ");
                        String tid = scanner.nextLine();

                        if (tid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        task = taskService.findTaskById(tid);
                        if (task == null) {
                            System.out.println("✖ Görev mevcut değil, tekrar deneyin.");
                        } else break;
                    }

                    try {
                        taskService.assignTaskToProject(task, project);
                        System.out.println("✔ Görev projeye atandı.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 7 -> { // Tüm görevleri listele
                    if (taskService.getAllTasks().isEmpty()
                            || projectService.getAllProjects().isEmpty()) {
                        System.out.println("Görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }
                    printAllTasksDetailed(taskService, userService, projectService);
                }

                case 0 -> {
                    running = false;
                    System.out.println("Programdan çıkılıyor...");
                }
            }

            System.out.println();
        }

        scanner.close();
    }

    // ------------------ UI YARDIMCILARI ------------------

    private static void returnToMainMenu(Scanner scanner) {
        System.out.print("Ana menüye dönmek için 0'a basın: ");
        while (!scanner.nextLine().equals("0")) {
            System.out.print("Lütfen 0'a basın: ");
        }
    }

    private static void printAllTasksSimple(TaskService ts) {
        for (Task t : ts.getAllTasks()) {
            System.out.println(t.getId() + " - " + t.getTitle());
        }
    }

    private static void printAllUsers(UserService us) {
        for (User u : us.getAllUsers()) {
            System.out.println(u.getId() + " - " + u.getName());
        }
    }

    private static void printAllProjects(ProjectService ps) {
        for (Project p : ps.getAllProjects()) {
            System.out.println(p.getId() + " - " + p.getName());
        }
    }

    private static void printAllTasksDetailed(
            TaskService ts, UserService us, ProjectService ps) {

        for (Task t : ts.getAllTasks()) {

            System.out.println("ID: " + t.getId());
            System.out.println("Ad: " + t.getTitle());

            String userName = "Yok";
            for (User u : us.getAllUsers()) {
                if (u.getTasks().contains(t)) {
                    userName = u.getName();
                    break;
                }
            }

            String projectName = "Yok";
            for (Project p : ps.getAllProjects()) {
                if (p.getTasks().contains(t)) {
                    projectName = p.getName();
                    break;
                }
            }

            System.out.println("Kullanıcı: " + userName);
            System.out.println("Proje: " + projectName);
            System.out.println("-------------------");
        }
    }
}
