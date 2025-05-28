package com.photodiary.backend.diary.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class MetadataExtractor {
    Metadata metadata;

    public void extractMetadata(MultipartFile multipartFile) {
        try {
            metadata = ImageMetadataReader.readMetadata(multipartFile.getInputStream());
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LocalDateTime getDateTime() {
        if(metadata == null || metadata.hasErrors()){
            throw new RuntimeException("메타데이터가 없습니다.");
        }

        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (directory == null) {
            throw new RuntimeException("EXIF 정보를 찾을 수 없습니다.");
        }

        Date date = directory.getDateOriginal(); // 촬영 시간
        if (date == null) {
            throw new RuntimeException("촬영 시간 정보가 없습니다.");
        }
        System.out.println("촬영 시간: " + date);

        LocalDateTime localDateTime = date.toInstant()   // Date -> Instant
                .atZone(ZoneId.systemDefault())  // Instant -> ZonedDateTime
                .toLocalDateTime();

        return localDateTime;
    }

    public GpsCoordinate getGpsCoordinate(){
        if(metadata == null || metadata.hasErrors()){
            throw new RuntimeException("메타데이터가 없습니다.");
        }

        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if (gpsDirectory == null) {
            throw new RuntimeException("GPS 정보가 없습니다.");
        }

        GeoLocation location = gpsDirectory.getGeoLocation();
        if(location == null){
            throw new RuntimeException("위치 정보가 없습니다.");
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        System.out.println("위도 (Latitude): " + latitude);
        System.out.println("경도 (Longitude): " + longitude);

        GpsCoordinate gpsPoint = new GpsCoordinate();
        gpsPoint.longitude = longitude;
        gpsPoint.latitude = latitude;
        return gpsPoint;
    }
}
