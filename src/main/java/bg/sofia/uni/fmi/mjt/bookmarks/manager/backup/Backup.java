package bg.sofia.uni.fmi.mjt.bookmarks.manager.backup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bg.sofia.uni.fmi.mjt.bookmarks.manager.exceptions.BackupException;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.DefaultUserManager;
import bg.sofia.uni.fmi.mjt.bookmarks.manager.user.UserManager;

/**
 * Backup
 */
public class Backup {
    public static final Path BACKUP_DIR = Path.of("bookmarks");
    // public static final Gson GSON = new GsonBuilder()
    //         .excludeFieldsWithoutExposeAnnotation()
    //         .create();
    public static final Gson GSON = new Gson();

    public static void backup(DefaultUserManager userManager) throws BackupException {
        Path backupJson = BACKUP_DIR.resolve("usermanager.backup.json");

        try (BufferedWriter bw = Files.newBufferedWriter(backupJson)) {
            Type token = TypeToken.getParameterized(DefaultUserManager.class).getType();

            GSON.toJson(userManager, token, bw);
        } catch (IOException e) {
            throw new BackupException("Couldn't write backup", e);
        }
    }

    public static UserManager load(Path backupJson) throws BackupException {
        try (BufferedReader br = Files.newBufferedReader(backupJson)) {
            return GSON.fromJson(br, DefaultUserManager.class);
        } catch (IOException e) {
            throw new BackupException("Couldn't load backup", e);
        }
    }

    // public static void backup(DefaultBookmarksStorage bookmarksStorage) {
    // Path userBackup = BACKUP_DIR.resolve(String.format("%s.json",
    // bookmarksStorage.getUser().getUsername()));
    //
    // try (BufferedWriter bw = Files.newBufferedWriter(userBackup)) {
    // Type token =
    // TypeToken.getParameterized(DefaultBookmarksStorage.class).getType();
    //
    // GSON.toJson(bookmarksStorage, token, bw);
    // } catch (IOException e) {
    // e.printStackTrace();
    // throw new NullPointerException();
    // }
    // }
    //
    // public static DefaultBookmarksStorage load(Path userBackup) {
    // try (BufferedReader br = Files.newBufferedReader(userBackup)) {
    // return GSON.fromJson(br, DefaultBookmarksStorage.class);
    // } catch (IOException e) {
    // e.printStackTrace();
    // throw new NullPointerException();
    // }
    // }
}
