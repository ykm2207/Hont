# HONT - 모션인식 기반 자세교정 홈트레이닝 앱

## 프로젝트 개요
컴공과 졸업 캡스톤 프로젝트. 모바일 카메라와 MediaPipe Pose를 활용하여 운동 자세를 실시간 분석·교정하고, 식단 관리 기능까지 통합한 홈트레이닝 앱.

## 기술 스택
- **Frontend**: Android (Kotlin) — Android Studio
- **Backend**: Java / Spring Boot — REST API 서버
- **DB**: MariaDB — 관계형 데이터베이스
- **AI**: Google MediaPipe Pose — 온디바이스 관절 인식 (외부 API 호출 없음)
- **인프라**: Cloudflare Tunnel
- **외부 API**: 식약처 식품영양성분 공공 API
- **인증**: OAuth 2.0 (Google, Kakao) + JWT

## 프로젝트 구조
```
hont/
├── app/               # Android 앱 (Kotlin)
├── backend/           # Spring Boot API 서버
└── docs/              # 설계 문서, ERD, API 명세 등
```

## 코딩 컨벤션
- 한국어 주석 사용
- Spring Boot: Java, 패키지 구조는 도메인 기반 (auth, workout, diet, community)
- Android: Kotlin, MVVM 아키텍처, XML 레이아웃 또는 Jetpack Compose
- API 응답 형식: JSON, RESTful 설계
- DB 테이블/컬럼명: snake_case

## 주요 기능 모듈
1. **인증 (auth)**: 소셜 로그인, JWT 발급/갱신
2. **운동 캘린더 (workout)**: 루틴 CRUD, 운동 기록
3. **자세 교정 (pose)**: MediaPipe 관절 좌표 → 각도 계산 → 자세 판별 (온디바이스)
4. **식단 관리 (diet)**: 식약처 API 연동, 음식 검색, 칼로리 계산
5. **커뮤니티 (community)**: 게시글, 댓글, 운동 인증, 루틴 공유

