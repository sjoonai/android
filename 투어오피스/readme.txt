투어오피스 다국어버전 웹뷰
- 네이버 MYBOX 풀소스 저장
- FCM 연동
- 로그인 시 FCM token과 userid 매칭 (자바스크립트 인터페이스 이용)



#
안드로이드와 웹페이지 연동을 위해서는  자바스크립트 인터페이스를 이용해야 한다.
자바스크립트에서 안드로이드 앱의 함수를 실행.

echo '<script>if (typeof android !== "undefined") android.setUserCheck("'.$mb_id.'");</script>'; //app
android 접근명은 앱의 MainActivity.java에 명시되어야 함.

 