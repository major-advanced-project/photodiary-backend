📸 PhotoDiary Backend

AI 기반 자동 일기 생성 & 사진 기록 서비스 백엔드 서버

포토다이어리는 사용자가 업로드한 사진을 기반으로 BLIP2 이미지 캡셔닝 · EXIF 메타데이터 분석 · GPT 기반 자연어 생성을 결합하여
자동으로 감성 일기를 생성하는 웹 서비스입니다.

백엔드는 Spring Boot + MySQL + AWS 인프라 + JWT 인증 구조로 구성되어 있으며
프론트엔드(Next.js/React) 및 AI 서버(FastAPI)와 연동됩니다.

<img width="545" height="296" alt="image" src="https://github.com/user-attachments/assets/c320e2f1-7d58-4af0-9510-e83b02b46387" />

🚀 주요 기능
| 구분                    | 기능 목록                                                                                                      |
| --------------------- | ---------------------------------------------------------------------------------------------------------- |
| **회원가입 & 로그인**        | - 이메일 기반 회원가입<br>- JWT 기반 인증/인가<br>- 비밀번호 암호화<br>- 중복 이메일 체크                                               |
| **사진 업로드 & 자동 일기 생성** | - EXIF 분석 (촬영 시간, GPS)<br>- GPS → 장소명 변환 (Kakao Map API)<br>- BLIP2 이미지 캡셔닝 (FastAPI)<br>- GPT 기반 감성 일기 생성 |
| **일기 관리 기능**          | - 내 일기 목록 조회<br>- 일기 상세보기<br>- 일기 수정<br>- 공개 여부 설정 (PUBLIC / PRIVATE)                                      |
| **친구 기능**             | - 친구 추가<br>- 친구 목록 조회<br>- 공개 일기만 조회<br>- 비공개 접근 차단                                                        |
<img width="568" height="501" alt="스크린샷 2025-11-20 오후 5 16 49" src="https://github.com/user-attachments/assets/5d9e60e4-93a4-4a92-b52a-c4d861a593c9" />


🧠 기술 스택
| 영역            | 사용 기술                                                                                                                             |
| ------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| **Backend**   | Java 17<br>Spring Boot<br>Spring Data JPA<br>Spring Security + JWT<br>metadata-extractor (EXIF 분석)<br>OpenAI GPT<br>Kakao Map API |
| **AI Server** | FastAPI<br>BLIP2 이미지 캡셔닝                                                                                                          |
| **Infra**     | AWS EC2 (Backend)<br>AWS S3 (이미지 저장)<br>AWS RDS (MySQL)                                                                           |


🧬 시스템 아키텍처
<img width="1008" height="634" alt="image" src="https://github.com/user-attachments/assets/8987ff83-8446-4dd6-a8be-31b0195901b8" />


🛠 API 요약
Auth API
| 메서드  | 엔드포인트              | 설명          |
| ---- | ------------------ | ----------- |
| POST | `/api/auth/signup` | 회원가입        |
| POST | `/api/auth/login`  | 로그인(JWT 발급) |

Diary API
| 메서드  | 엔드포인트                 | 설명       |
| ---- | --------------------- | -------- |
| POST | `/api/diary/generate` | 자동 일기 생성 |
| POST | `/api/diary`          | 일기 저장    |
| GET  | `/api/diary/me`       | 내 일기 조회  |
| GET  | `/api/diary/{id}`     | 일기 상세    |
| PUT  | `/api/diary/{id}`     | 일기 수정    |

Friend API
| 메서드  | 엔드포인트                          | 설명          |
| ---- | ------------------------------ | ----------- |
| POST | `/api/friend`                  | 친구 추가       |
| GET  | `/api/friend/list`             | 친구 목록       |
| GET  | `/api/friend/{friendId}/diary` | 친구 공개 일기 조회 |

📡 AI 서버 연동 흐름
| 단계               | 설명                                        |
| ---------------- | ----------------------------------------- |
| **1. 이미지 업로드**   | User → Backend : 사용자 이미지 업로드              |
| **2. 이미지 캡션 요청** | Backend → BLIP2 Server : 이미지 전송           |
| **3. 캡션 반환**     | BLIP2 Server → Backend : 이미지 설명(캡션) 반환    |
| **4. 장소명 변환**    | Backend → KakaoMap API : GPS 좌표 → 장소명 변환  |
| **5. 일기 생성 요청**  | Backend → GPT API : 이미지·메타데이터 기반 일기 생성 요청 |
| **6. 생성된 일기 수신** | GPT API → Backend : 감성 일기 텍스트 반환          |
| **7. 사용자에게 전달**  | Backend → User : 최종 생성된 일기 전달             |



🗄 ERD
<img width="1072" height="898" alt="image" src="https://github.com/user-attachments/assets/1fde39d7-02b5-4d56-8b9e-51fce2a5fe50" />


⚙ 실행 방법
| 항목               | 변수명                                                                                     |
| ---------------- | --------------------------------------------------------------------------------------- |
| **JWT 설정**       | `JWT_SECRET`                                                                            |
| **DB 설정**        | `SPRING_DATASOURCE_URL`<br>`SPRING_DATASOURCE_USERNAME`<br>`SPRING_DATASOURCE_PASSWORD` |
| **AWS S3 설정**    | `AWS_S3_BUCKET`<br>`AWS_ACCESS_KEY`<br>`AWS_SECRET_KEY`                                 |
| **AI 서버 설정**     | `BLIP2_SERVER_URL`                                                                      |
| **OpenAI 설정**    | `OPENAI_API_KEY`                                                                        |
| **Kakao API 설정** | `KAKAO_REST_API_KEY`                                                                    |


2) 빌드 & 실행  
| 단계          | 명령어                                           |
| ----------- | --------------------------------------------- |
| **프로젝트 빌드** | `./gradlew build`                             |
| **JAR 실행**  | `java -jar build/libs/photodiary-backend.jar` |




🔍 테스트 결과 요약
| 항목       | 결과                      |
| -------- | ----------------------- |
| 회원가입/로그인 | 정상                      |
| 자동 일기 생성 | 1~2장 매우 안정적 / 3장↑ 지연 발생 |
| AI 서버 연동 | BLIP2 처리량 많으면 속도 저하     |
| 친구 기능    | 정상                      |
| AWS 인프라  | 평균 응답속도 300ms           |

👥 팀원
| 이름  | 역할                    |
| --- | --------------------- |
| 김민석 | GPT 일기 생성, API 개발     |
| 남경식 | BLIP2 서버, AWS 인프라     |
| 장익환 | 백엔드 API, FE-서버 연동, 배포 |

📌 향후 개선 방향
| 항목                 | 내용                              |
| ------------------ | ------------------------------- |
| **BLIP2 모델 고도화**   | 고성능 BLIP2 모델 적용으로 이미지 캡셔닝 품질 향상 |
| **GPT 연동 최적화**     | 이미지가 많을 때 발생하는 GPT 타임아웃 문제 개선   |
| **해외 위치 인식 강화**    | 해외 GPS 좌표에 대한 장소명 추출 정확도 향상     |
| **AI 파이프라인 속도 개선** | 전체 이미지 → 캡션 → GPT 흐름 속도 최적화     |


📎 관련 저장소
| 영역              | Repository                                                                                                                           |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| Backend         | [https://github.com/major-advanced-project/photodiary-backend](https://github.com/major-advanced-project/photodiary-backend)         |
| Frontend        | [https://github.com/major-advanced-project/photodiary-frontend](https://github.com/major-advanced-project/photodiary-frontend)       |
| BLIP2 AI Server | [https://github.com/major-advanced-project/photodiary-blip-server](https://github.com/major-advanced-project/photodiary-blip-server) |

