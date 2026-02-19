import model.*;
import service.ExportService;
import service.NotificationService;
import service.ProjectService;
import service.TaskService;
import service.UserService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        NotificationService notificationService = new NotificationService();
        TaskService taskService = new TaskService(notificationService);
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();
        ExportService exportService = new ExportService();

        // Program başlarken verileri yükle
        String dataFile = "data.txt";
        java.io.File file = new java.io.File(dataFile);
        if (file.exists()) {
            System.out.println("Veriler yükleniyor...");
            if (exportService.importFromSimpleFormat(taskService, projectService, userService, dataFile)) {
                System.out.println(" Veriler başarıyla yüklendi.");
            } else {
                System.out.println(" Veri yükleme hatası.");
            }
            System.out.println();
        }


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
            System.out.println("10 - Bildirimleri Görüntüle");
            System.out.println("11 - Verileri Dosyaya Aktar");
            System.out.println("12 - Verileri Dosyadan Yükle");
            System.out.println("13 - Verileri Sil");
            System.out.println("0 - Çıkış");
            System.out.print("Seçiminiz: ");

            int secim = scanner.nextInt();
            scanner.nextLine();

            switch (secim) {

                // 1️⃣ Görev oluştur
                case 1 -> {

                    String id;
                    while (true) {
                        System.out.print("Görev ID (0: Ana Menü): ");
                        id = scanner.nextLine();

                        if (id.equals("0")) {
                            break;
                        }

                        if (taskService.taskExists(id)) {
                            System.out.println("✖ Bu ID ile görev zaten var.");
                            continue;
                        }
                        break;
                    }

                    if (!id.equals("0")) {
                        System.out.print("Başlık: ");
                        String title = scanner.nextLine();

                        System.out.print("Açıklama: ");
                        String desc = scanner.nextLine();

                        Priority priority = readPriority(scanner);

                        try {
                            Task task = taskService.createTask(id, title, desc);
                            task.setPriority(priority);
                            System.out.println("✔ Görev oluşturuldu.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("✖ " + e.getMessage());
                        }
                    }
                }

                // 2️⃣ Süreli görev oluştur
                case 2 -> {

                    String id;
                    while (true) {
                        System.out.print("Görev ID (0: Ana Menü): ");
                        id = scanner.nextLine();

                        if (id.equals("0")) {
                            break;
                        }

                        if (taskService.taskExists(id)) {
                            System.out.println("✖ Bu ID ile görev zaten var.");
                            continue;
                        }
                        break;
                    }

                    if (!id.equals("0")) {
                        System.out.print("Başlık: ");
                        String title = scanner.nextLine();

                        System.out.print("Açıklama: ");
                        String desc = scanner.nextLine();

                        Priority priority = readPriority(scanner);

                        LocalDate deadline = readValidDate(scanner);
                        if (deadline != null) {
                            try {
                                TimedTask task = taskService.createTimedTask(id, title, desc, deadline);
                                task.setPriority(priority);
                                System.out.println("✔ Süreli görev oluşturuldu.");
                            } catch (IllegalArgumentException e) {
                                System.out.println("✖ " + e.getMessage());
                            }
                        }
                    }
                }

                // 3️⃣ Görev tamamla
                case 3 -> {

                    if (!taskService.hasTasks()) {
                        System.out.println("✖ Hiç görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Tamamlanacak Görev ID (0): ");
                        String id = scanner.nextLine();

                        if (id.equals("0")) {
                            returnToMainMenu(scanner);
                            break;
                        }

                        if (taskService.completeTask(id)) {
                            System.out.println("✔ Görev tamamlandı.");
                            break;
                        } else {
                            System.out.println("✖ Görev bulunamadı.");
                        }
                    }
                }

                // 4️⃣ Görevi kullanıcıya ata
                case 4 -> {

                    if (!taskService.canAssignTaskToUser() || !userService.hasUsers()) {
                        System.out.println("✖ Görev veya kullanıcı yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    User user;
                    while (true) {
                        printAllUsers(userService);
                        System.out.print("Kullanıcı ID (0): ");
                        String uid = scanner.nextLine();

                        if (uid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        user = userService.findUserById(uid);
                        if (user != null) break;

                        System.out.println("✖ Kullanıcı bulunamadı.");
                    }

                    Task task;
                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Görev ID (0): ");
                        String tid = scanner.nextLine();

                        if (tid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        task = taskService.findTaskById(tid);
                        if (task != null) break;

                        System.out.println("✖ Görev bulunamadı.");
                    }

                    taskService.assignTaskToUser(task, user);
                    System.out.println("✔ Görev kullanıcıya atandı.");
                }

                // 5️⃣ Görevi projeye ata
                case 5 -> {

                    if (!taskService.canAssignTaskToProject() || !projectService.hasProjects()) {
                        System.out.println("✖ Görev veya proje yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    Project project;
                    while (true) {
                        printAllProjects(projectService);
                        System.out.print("Proje ID (0): ");
                        String pid = scanner.nextLine();

                        if (pid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        project = projectService.findProjectById(pid);
                        if (project != null) break;

                        System.out.println("✖ Proje bulunamadı.");
                    }

                    Task task;
                    while (true) {
                        printAllTasksSimple(taskService);
                        System.out.print("Görev ID (0): ");
                        String tid = scanner.nextLine();

                        if (tid.equals("0")) {
                            returnToMainMenu(scanner);
                            return;
                        }

                        task = taskService.findTaskById(tid);
                        if (task != null) break;

                        System.out.println("✖ Görev bulunamadı.");
                    }

                    taskService.assignTaskToProject(task, project);
                    System.out.println("✔ Görev projeye atandı.");
                }

                // 6️⃣ Yaklaşan görevler
                case 6 -> {

                    List<TimedTask> upcoming = taskService.getUpcomingTasks();

                    if (upcoming.isEmpty()) {
                        System.out.println("Yaklaşan görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    for (TimedTask t : upcoming) {

                        LocalDate due = t.getDeadline().getDueDate();
                        long remaining = ChronoUnit.DAYS.between(LocalDate.now(), due);

                        String projectName = "Yok";
                        String projectDescription = "";
                        for (Project p : projectService.getAllProjects()) {
                            if (p.getTasks().contains(t)) {
                                projectName = p.getName();
                                projectDescription = p.getDescription();
                                break;
                            }
                        }

                        System.out.println("ID: " + t.getId());
                        System.out.println("Ad: " + t.getTitle());
                        System.out.println("Açıklama: " + t.getDescription());
                        System.out.println("Öncelik: " + t.getPriority());
                        System.out.println("Proje: " + projectName);
                        if (!projectDescription.isEmpty()) {
                            System.out.println("Proje Açıklaması: " + projectDescription);
                        }
                        System.out.println("Deadline: " + due);
                        System.out.println("Kalan Gün: " + remaining);
                        System.out.println("------------------");
                    }

                    returnToMainMenu(scanner);
                }

                // 7️⃣ Tüm görevleri listele
                case 7 -> {

                    if (!taskService.hasTasks()) {
                        System.out.println("Görev yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    for (Task t : taskService.getAllTasks()) {

                        System.out.println("ID: " + t.getId());
                        System.out.println("Ad: " + t.getTitle());
                        System.out.println("Açıklama: " + t.getDescription());
                        System.out.println("Öncelik: " + t.getPriority());
                        System.out.println("Durum: " + (t.isCompleted() ? "Tamamlandı" : "Devam Ediyor"));

                        if (t instanceof TimedTask tt) {
                            LocalDate due = tt.getDeadline().getDueDate();
                            long remaining = ChronoUnit.DAYS.between(LocalDate.now(), due);
                            System.out.println("Deadline: " + due);
                            System.out.println("Kalan Gün: " + remaining);
                        } else {
                            System.out.println("Deadline: Yok");
                        }

                        String userName = "Yok";
                        for (User u : userService.getAllUsers()) {
                            if (u.getTasks().contains(t)) {
                                userName = u.getName();
                                break;
                            }
                        }

                        String projectName = "Yok";
                        String projectDescription = "";
                        boolean projectCompleted = false;
                        for (Project p : projectService.getAllProjects()) {
                            if (p.getTasks().contains(t)) {
                                projectName = p.getName();
                                projectDescription = p.getDescription();
                                projectCompleted = p.isCompleted();
                                break;
                            }
                        }

                        System.out.println("Kullanıcı: " + userName);
                        System.out.println("Proje: " + projectName);
                        if (!projectName.equals("Yok")) {
                            System.out.println("Proje Durumu: " + (projectCompleted ? "Tamamlandı" : "Devam Ediyor"));
                        }
                        if (!projectDescription.isEmpty()) {
                            System.out.println("Proje Açıklaması: " + projectDescription);
                        }
                        System.out.println("----------------------------");
                    }

                    returnToMainMenu(scanner);
                }

                // 8️⃣ Kullanıcı ekle
                case 8 -> {
                    String userId;
                    while (true) {
                        System.out.print("Kullanıcı ID (0: Ana Menü): ");
                        userId = scanner.nextLine();

                        if (userId.equals("0")) {
                            break;
                        }

                        if (userService.userExists(userId)) {
                            System.out.println("✖ Bu ID ile kullanıcı zaten var.");
                        } else break;
                    }

                    if (!userId.equals("0")) {
                        System.out.print("Kullanıcı Adı: ");
                        String userName = scanner.nextLine();

                        try {
                            userService.addUser(userId, userName);
                            System.out.println("✔ Kullanıcı eklendi.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("✖ " + e.getMessage());
                        }
                    }
                }

                // 9️⃣ Proje ekle
                case 9 -> {
                    String projectId;
                    while (true) {
                        System.out.print("Proje ID (0: Ana Menü): ");
                        projectId = scanner.nextLine();

                        if (projectId.equals("0")) {
                            break;
                        }

                        if (projectService.projectExists(projectId)) {
                            System.out.println("✖ Bu ID ile proje zaten var.");
                        } else break;
                    }

                    if (!projectId.equals("0")) {
                        System.out.print("Proje Adı: ");
                        String projectName = scanner.nextLine();

                        try {
                            projectService.createProject(projectId, projectName);
                            System.out.println("✔ Proje eklendi.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("✖ " + e.getMessage());
                        }
                    }
                }

                // 🔔 Bildirimleri görüntüle
                case 10 -> {

                    if (!notificationService.hasNotifications()) {
                        System.out.println("Bildirim yok.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    for (Notification n : notificationService.getAllNotifications()) {

                        System.out.println("Mesaj: " + n.getMessage());
                        System.out.println("Tarih: " + n.getCreatedAt());
                        System.out.println("Görev: " + n.getRelatedTask().getTitle());
                        System.out.println("-------------------");
                    }

                    returnToMainMenu(scanner);
                }

                // 💾 Verileri dosyaya aktar
                case 11 -> {
                    System.out.println("Dosyaya Aktarma Seçenekleri:");
                    System.out.println("1 - Tüm Görevleri Aktar");
                    System.out.println("2 - Tüm Projeleri Aktar");
                    System.out.println("3 - Tüm Kullanıcıları Aktar");
                    System.out.println("4 - Tüm Verileri Aktar (Hepsi)");
                    System.out.print("Seçiminiz (0: Ana Menü): ");

                    String exportChoice = scanner.nextLine();

                    if (exportChoice.equals("0")) {
                        break;
                    }

                    System.out.print("Dosya adı (örn: rapor.txt): ");
                    String fileName = scanner.nextLine();

                    if (fileName.trim().isEmpty()) {
                        System.out.println("✖ Dosya adı boş olamaz.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    boolean success = false;

                    switch (exportChoice) {
                        case "1" -> {
                            success = exportService.exportAllTasksToFile(
                                    taskService, projectService, userService, fileName);
                            if (success) {
                                System.out.println("✔ Görevler '" + fileName + "' dosyasına aktarıldı.");
                                // Verileri otomatik kaydet
                                exportService.saveDataToFile(taskService, projectService, userService, "data.txt");
                            } else {
                                System.out.println("✖ Dosyaya aktarma başarısız oldu.");
                            }
                        }
                        case "2" -> {
                            success = exportService.exportAllProjectsToFile(projectService, fileName);
                            if (success) {
                                System.out.println("✔ Projeler '" + fileName + "' dosyasına aktarıldı.");
                                // Verileri otomatik kaydet
                                exportService.saveDataToFile(taskService, projectService, userService, "data.txt");
                            } else {
                                System.out.println("✖ Dosyaya aktarma başarısız oldu.");
                            }
                        }
                        case "3" -> {
                            success = exportService.exportAllUsersToFile(userService, fileName);
                            if (success) {
                                System.out.println("✔ Kullanıcılar '" + fileName + "' dosyasına aktarıldı.");
                                // Verileri otomatik kaydet
                                exportService.saveDataToFile(taskService, projectService, userService, "data.txt");
                            } else {
                                System.out.println("✖ Dosyaya aktarma başarısız oldu.");
                            }
                        }
                        case "4" -> {
                            success = exportService.exportAllToFile(
                                    taskService, projectService, userService, fileName);
                            if (success) {
                                System.out.println("✔ Tüm veriler '" + fileName + "' dosyasına aktarıldı.");
                                // Verileri otomatik kaydet
                                exportService.saveDataToFile(taskService, projectService, userService, "data.txt");
                            } else {
                                System.out.println("✖ Dosyaya aktarma başarısız oldu.");
                            }
                        }
                        default -> {
                            System.out.println("✖ Geçersiz seçim.");
                            returnToMainMenu(scanner);
                            break;
                        }
                    }

                    if (success) {
                        returnToMainMenu(scanner);
                    }
                }

                // 📥 Verileri dosyadan yükle
                case 12 -> {
                    System.out.print("Dosya adı (örn: data.txt): ");
                    String fileName = scanner.nextLine();

                    if (fileName.trim().isEmpty()) {
                        System.out.println("✖ Dosya adı boş olamaz.");
                        returnToMainMenu(scanner);
                        break;
                    }

                    java.io.File importFile = new java.io.File(fileName);
                    if (!importFile.exists()) {
                        System.out.println("✖ Dosya bulunamadı: " + fileName);
                        returnToMainMenu(scanner);
                        break;
                    }

                    System.out.println("Veriler yükleniyor...");
                    if (exportService.importFromSimpleFormat(taskService, projectService, userService, fileName)) {
                        System.out.println("✔ Veriler başarıyla yüklendi.");
                    } else {
                        System.out.println("✖ Veri yükleme hatası.");
                    }

                    returnToMainMenu(scanner);
                }

                // 🗑️ Verileri sil
                case 13 -> {
                    System.out.println("Silme Seçenekleri:");
                    System.out.println("1 - Görev Sil");
                    System.out.println("2 - Proje Sil");
                    System.out.println("3 - Kullanıcı Sil");
                    System.out.println("4 - Tüm Görevleri Sil");
                    System.out.println("5 - Tüm Projeleri Sil");
                    System.out.println("6 - Tüm Kullanıcıları Sil");
                    System.out.println("7 - TÜM VERİLERİ SİL (Dikkatli!)");
                    System.out.print("Seçiminiz (0: Ana Menü): ");

                    String deleteChoice = scanner.nextLine();

                    if (deleteChoice.equals("0")) {
                        break;
                    }

                    switch (deleteChoice) {
                        // Görev sil
                        case "1" -> {
                            if (!taskService.hasTasks()) {
                                System.out.println(" Silinecek görev yok.");
                                returnToMainMenu(scanner);
                                break;
                            }

                            while (true) {
                                printAllTasksSimple(taskService);
                                System.out.print("Silinecek Görev ID (0: İptal): ");
                                String taskId = scanner.nextLine();

                                if (taskId.equals("0")) {
                                    break;
                                }

                                Task task = taskService.findTaskById(taskId);
                                if (task == null) {
                                    System.out.println(" Görev bulunamadı.");
                                    continue;
                                }

                                // Kullanıcılardan görevi kaldır
                                for (User user : userService.getAllUsers()) {
                                    user.getTasks().remove(task);
                                }

                                // Projelerden görevi kaldır
                                for (Project project : projectService.getAllProjects()) {
                                    project.getTasks().remove(task);
                                }

                                // Görevi sil
                                if (taskService.deleteTask(taskId)) {
                                    System.out.println(" Görev silindi.");
                                    break;
                                } else {
                                    System.out.println(" Görev silinemedi.");
                                }
                            }
                        }

                        // Proje sil
                        case "2" -> {
                            if (!projectService.hasProjects()) {
                                System.out.println(" Silinecek proje yok.");
                                returnToMainMenu(scanner);
                                break;
                            }

                            while (true) {
                                printAllProjects(projectService);
                                System.out.print("Silinecek Proje ID (0: İptal): ");
                                String projectId = scanner.nextLine();

                                if (projectId.equals("0")) {
                                    break;
                                }

                                if (projectService.deleteProject(projectId)) {
                                    System.out.println(" Proje silindi.");
                                    break;
                                } else {
                                    System.out.println(" Proje bulunamadı.");
                                }
                            }
                        }

                        // Kullanıcı sil
                        case "3" -> {
                            if (!userService.hasUsers()) {
                                System.out.println("✖ Silinecek kullanıcı yok.");
                                returnToMainMenu(scanner);
                                break;
                            }

                            while (true) {
                                printAllUsers(userService);
                                System.out.print("Silinecek Kullanıcı ID (0: İptal): ");
                                String userId = scanner.nextLine();

                                if (userId.equals("0")) {
                                    break;
                                }

                                if (userService.deleteUser(userId)) {
                                    System.out.println(" Kullanıcı silindi.");
                                    break;
                                } else {
                                    System.out.println(" Kullanıcı bulunamadı.");
                                }
                            }
                        }

                        // Tüm görevleri sil
                        case "4" -> {
                            if (!taskService.hasTasks()) {
                                System.out.println(" Silinecek görev yok.");
                                returnToMainMenu(scanner);
                                break;
                            }

                            System.out.print("Tüm görevleri silmek istediğinize emin misiniz? (EVET yazın): ");
                            String confirm = scanner.nextLine();

                            if (confirm.equals("EVET")) {
                                // Tüm kullanıcılardan görevleri kaldır
                                for (User user : userService.getAllUsers()) {
                                    user.getTasks().clear();
                                }

                                // Tüm projelerden görevleri kaldır
                                for (Project project : projectService.getAllProjects()) {
                                    project.getTasks().clear();
                                }

                                taskService.deleteAllTasks();
                                System.out.println(" Tüm görevler silindi.");
                            } else {
                                System.out.println(" İşlem iptal edildi.");
                            }
                        }

                        // Tüm projeleri sil
                        case "5" -> {
                            if (!projectService.hasProjects()) {
                                System.out.println(" Silinecek proje yok.");
                                returnToMainMenu(scanner);
                                break;
                            }

                            System.out.print("Tüm projeleri silmek istediğinize emin misiniz? (EVET yazın): ");
                            String confirm = scanner.nextLine();

                            if (confirm.equals("EVET")) {
                                projectService.deleteAllProjects();
                                System.out.println("Tüm projeler silindi.");
                            } else {
                                System.out.println(" İşlem iptal edildi.");
                            }
                        }

                        // Tüm kullanıcıları sil
                        case "6" -> {
                            if (!userService.hasUsers()) {
                                System.out.println("✖ Silinecek kullanıcı yok.");
                                returnToMainMenu(scanner);
                                break;
                            }

                            System.out.print("Tüm kullanıcıları silmek istediğinize emin misiniz? (EVET yazın): ");
                            String confirm = scanner.nextLine();

                            if (confirm.equals("EVET")) {
                                userService.deleteAllUsers();
                                System.out.println("✔ Tüm kullanıcılar silindi.");
                            } else {
                                System.out.println("✖ İşlem iptal edildi.");
                            }
                        }

                        // TÜM VERİLERİ SİL
                        case "7" -> {
                            System.out.println("  UYARI: Bu işlem TÜM VERİLERİ silecektir!");
                            System.out.print("Emin misiniz? (TÜMÜNÜSİL yazın): ");
                            String confirm = scanner.nextLine();

                            if (confirm.equals("TÜMÜNÜSİL")) {
                                taskService.deleteAllTasks();
                                projectService.deleteAllProjects();
                                userService.deleteAllUsers();
                                System.out.println("✔ Tüm veriler silindi.");
                            } else {
                                System.out.println("✖ İşlem iptal edildi.");
                            }
                        }

                        default -> {
                            System.out.println("✖ Geçersiz seçim.");
                            returnToMainMenu(scanner);
                            break;
                        }
                    }

                    returnToMainMenu(scanner);
                }

                case 0 -> {
                    // Program kapanırken verileri kaydet
                    System.out.println("Veriler kaydediliyor...");
                    exportService.saveDataToFile(taskService, projectService, userService, "data.txt");
                    running = false;
                    System.out.println("Programdan çıkılıyor...");
                }
            }

            System.out.println();
        }

        scanner.close();
    }

    // -------- YARDIMCI METODLAR --------

    private static Priority readPriority(Scanner scanner) {
        while (true) {
            System.out.println("Öncelik Seç:");
            System.out.println("1 - LOW");
            System.out.println("2 - MEDIUM");
            System.out.println("3 - HIGH");
            System.out.print("Seçim: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1": return Priority.LOW;
                case "2": return Priority.MEDIUM;
                case "3": return Priority.HIGH;
                default:
                    System.out.println(" Geçersiz seçim, tekrar deneyin.");
            }
        }
    }

    private static LocalDate readValidDate(Scanner scanner) {
        while (true) {
            System.out.print("Deadline (YYYY-MM-DD) (0): ");
            String input = scanner.nextLine();

            if (input.equals("0")) return null;

            try {
                LocalDate date = LocalDate.parse(input);

                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Geçmiş tarih girilemez.");
                    continue;
                }

                return date;

            } catch (Exception e) {
                System.out.println(" Geçersiz tarih formatı. Örnek: 2024-05-12");
            }
        }
    }

    private static void returnToMainMenu(Scanner scanner) {
        System.out.print("Ana menüye dönmek için 0'a basın: ");
        while (!scanner.nextLine().equals("0")) {
            System.out.print("Lütfen 0'a basın: ");
        }
    }

    private static void printAllTasksSimple(TaskService ts) {
        for (Task t : ts.getAllTasks()) {
            System.out.println(t.getId() + " - " + t.getTitle()
                    + " (" + t.getPriority() + ")");
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
}
