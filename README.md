📸 PhotoDiary Backend

AI 기반 자동 일기 생성 & 사진 기록 서비스 백엔드 서버

포토다이어리는 사용자가 업로드한 사진을 기반으로 BLIP2 이미지 캡셔닝 · 메타데이터 분석 · GPT 기반 자연어 생성을 결합하여 자동 감성 일기 생성을 지원하는 웹 서비스입니다.
백엔드는 Spring Boot + MySQL + AWS 인프라 + JWT 인증 구조로 구현되며, 프론트엔드(Next.js/React) 및 AI 서버(FastAPI)와 연동됩니다.

본 README는 제공된 공식 문서를 기반으로 작성되었습니다.
요구사항 분석서 / 시스템 설계 문서 / 최종보고서

📂 프로젝트 구조
photodiary-backend
 ├── src
 │   ├── controller      # API 엔드포인트
 │   ├── service         # 비즈니스 로직
 │   ├── repository      # JPA Repository
 │   ├── domain          # Entity
 │   ├── jwt             # JWT 토큰 생성/검증
 │   ├── modules         # ImageToText / KakaoMap 등 연동 모듈
 │   └── dto             # 요청/응답 DTO
 ├── build.gradle
 └── application.yml

🚀 주요 기능
1) 회원가입 & 로그인

이메일 기반 회원가입

JWT 기반 인증/인가

비밀번호 암호화

중복 이메일 체크
➡️ UserAuthModule 기반 기능

2) 사진 업로드 & 자동 일기 생성

업로드된 이미지에 대해 다음 과정 수행:

EXIF 메타데이터 추출 (촬영 시간, GPS)

카카오맵 Reverse Geocoding → 장소명 변환

BLIP2 이미지 캡션 생성 (FastAPI AI 서버 호출)

GPT 프롬프트 생성 & 일기 자동 생성

➡️ 시스템 요구사항 및 알고리즘 설명 참고




3) 일기 관리 기능

내 일기 목록 조회

일기 상세 보기

일기 수정

공개 여부 설정 (PUBLIC / PRIVATE)

4) 친구 기능

친구 추가

친구 목록 조회

친구의 공개 일기만 조회 가능

비공개 일기 접근 시 예외 처리
➡️ FriendModule 기반 구현

🧠 기술 스택
Backend

Java 17

Spring Boot

Spring Data JPA

Spring Security + JWT

metadata-extractor (EXIF 분석)

AI Server

FastAPI + Python

BLIP2 이미지 캡셔닝

OpenAI GPT 기반 일기 생성

Kakao Map API (좌표 → 장소명)

Infra

AWS EC2 (백엔드)

AWS S3 (이미지 저장)

AWS RDS(MySQL) (DB)

GitHub Actions 기반 CI/CD 계획

🧬 시스템 아키텍처

Spring Boot Backend ↔ BLIP2 AI Server ↔ OpenAI GPT ↔ KakaoMap API ↔ MySQL
문서 기반 아키텍처 이미지 참고:

🛠 API 요약
Auth API
메서드	엔드포인트	설명
POST	/api/auth/signup	회원가입
POST	/api/auth/login	로그인(JWT 발급)
Diary API
메서드	엔드포인트	설명
POST	/api/diary/generate	자동 일기 생성 (BLIP2 + GPT)
POST	/api/diary	일기 저장
GET	/api/diary/me	내 일기 조회
GET	/api/diary/{id}	일기 상세 조회
PUT	/api/diary/{id}	일기 수정
Friend API
메서드	엔드포인트	설명
POST	/api/friend	친구 추가
GET	/api/friend/list	친구 목록 조회
GET	/api/friend/{friendId}/diary	친구 공개 일기 조회
📡 AI 서버 연동 흐름
sequenceDiagram
User ->> Backend: 이미지 업로드
Backend ->> BLIP2 Server: 이미지 전송
BLIP2 Server ->> Backend: 이미지 설명 캡션
Backend ->> KakaoMap API: GPS → 장소명
Backend ->> GPT API: 프롬프트 생성 및 일기 요청
GPT API ->> Backend: 일기 텍스트 반환
Backend ->> User: 생성된 일기 전달


🗄 DB 스키마


⚙️ 실행 방법
1) 필수 환경 변수
JWT_SECRET=
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

AWS_S3_BUCKET=
AWS_ACCESS_KEY=
AWS_SECRET_KEY=

BLIP2_SERVER_URL=
OPENAI_API_KEY=
KAKAO_REST_API_KEY=

2) 빌드 & 실행
./gradlew build
java -jar build/libs/photodiary-backend.jar

🔍 테스트 결과 요약

시험 항목	결과
회원가입 / 로그인	정상 동작
자동 일기 생성	1~2장: 성공률 높음 / 3장↑: 지연 발생
BLIP2 ↔ GPT 연동	입력 많을 시 지연
친구 기능	정상 동작 & 예외 처리 완료
AWS 인프라	평균 응답 300ms
👥 팀원 소개

이름	역할
김민석	백엔드 API, GPT 일기 생성
남경식	BLIP2 서버 개발, AWS 인프라
장익환	백엔드 API, FE-서버 연동, 배포
📌 향후 개선 계획

BLIP2 고성능 모델 도입 및 속도 개선

다중 이미지 처리 시 GPT 타임아웃 해결

인물/장소 정확도 향상

전체 파이프라인 비동기화 및 최적화

📎 관련 저장소
영역	Repository
Backend	https://github.com/major-advanced-project/photodiary-backend

Frontend	https://github.com/major-advanced-project/photodiary-frontend

BLIP2 AI Server	https://github.com/major-advanced-project/photodiary-blip-server
