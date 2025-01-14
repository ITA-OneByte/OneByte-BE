package classfit.example.classfit.drive.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

public enum FileType {
    FOLDER("folder"),
    DOCUMENT("pdf", "doc", "docx", "txt", "xls", "xlsx"),
    IMAGE("jpg", "jpeg", "png", "gif", "bmp", "tiff"),
    VIDEO("mp4", "avi", "mkv", "mov", "wmv", "flv"),
    AUDIO("mp3", "wav", "aac", "flac", "ogg"),
    ARCHIVE("zip", "tar", "gz", "rar", "7z"),
    OTHER("other"),
    UNKNOWN("");

    private final List<String> extensions;

    FileType(String... extensions) {
        this.extensions = List.of(extensions);
    }

    public boolean hasExtension(String extension) {
        return extensions.contains(extension.toLowerCase());
    }

    public static FileType getFileTypeByExtension(String extension) {
        for (FileType type : values()) {
            if (type.hasExtension(extension)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public static String getContentType(String extension) {
        Map<String, String> mimeTypes = new HashMap<>();
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("txt", "text/plain");
        mimeTypes.put("html", "text/html");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "application/javascript");
        mimeTypes.put("json", "application/json");

        return mimeTypes.getOrDefault(extension, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }
}
