package com.photodiary.backend.diary.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class MetadataExtractor {

    private Metadata metadata;

    /**
     * 이미지 파일로부터 메타데이터를 추출합니다.
     */
    public void extractMetadata(File file) {
        try {
            this.metadata = ImageMetadataReader.readMetadata(file);
            System.out.println("[✅] 메타데이터 추출 성공: " + file.getName());

            // 디버깅용 전체 태그 출력
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag);
                }
            }

        } catch (ImageProcessingException | IOException e) {
            System.err.println("[❌] 메타데이터 추출 실패: " + e.getMessage());
            this.metadata = null;
        }
    }

    /**
     * 촬영 일시를 반환합니다. (없을 경우 LocalDateTime.now() 반환)
     */
    public LocalDateTime getDateTime() {
        if (metadata == null) {
            System.out.println("[⚠️] 메타데이터 없음 → 현재 시간 반환");
            return LocalDateTime.now();
        }

        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (directory == null) {
            System.out.println("[⚠️] EXIF 디렉토리 없음 → 현재 시간 반환");
            return LocalDateTime.now();
        }

        Date date = Optional.ofNullable(directory.getDateOriginal())
                .orElseGet(() -> Optional.ofNullable(directory.getDateDigitized())
                        .orElse(directory.getDateModified()));

        if (date == null) {
            System.out.println("[⚠️] 촬영 시간 없음 → 현재 시간 반환");
            return LocalDateTime.now();
        }

        System.out.println("[📷] 촬영 시간 추출됨: " + date);
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * GPS 좌표를 반환합니다. (없을 경우 null 반환)
     */
    public GpsCoordinate getGpsCoordinate() {
        if (metadata == null) {
            System.out.println("[⚠️] 메타데이터 없음 → GPS 좌표 추출 실패");
            return null;
        }

        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if (gpsDirectory == null) {
            System.out.println("[⚠️] GPS 디렉토리 없음 → GPS 좌표 추출 실패");
            return null;
        }

        GeoLocation location = gpsDirectory.getGeoLocation();
        if (location == null || Double.isNaN(location.getLatitude()) || Double.isNaN(location.getLongitude())) {
            System.out.println("[⚠️] GPS 정보 없음 또는 잘못된 좌표");
            return null;
        }

        GpsCoordinate gpsPoint = new GpsCoordinate();
        gpsPoint.latitude = location.getLatitude();
        gpsPoint.longitude = location.getLongitude();
        System.out.printf("[📍] 위도: %.6f, 경도: %.6f\n", gpsPoint.latitude, gpsPoint.longitude);

        return gpsPoint;
    }
}
