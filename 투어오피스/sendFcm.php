<?php
/*
 * FCM Message Send
 */
include '../include/top_proc.html';

$users = array('f6PRhf6mRJ6Tlh61qfTKGP:APA91bH1KvH35mihjH_o5pH6_e0lAZiNRXHDGelBhyT6YX3i5gP9td1iBYcRbFVyU3S40YBOvkCJmdHsGHu2vUerpmKyPPixqCIiRfpb34TzPVe70ThZl_WBpnq8U1lClxirAFdYDCxT');
$fcmKey = 'AAAAb9bj-Wo:APA91bHoVHz7T2B2ptiie41AJglDDm8Yr0DtZu3G-iOTERHAoE9-pNIFMEzsuDqHzizR-WqzsGCfTe0RzsJSjCBnL5U7zDkM22nELsmPfgv_r5YAi1XGqTS50-oJ0n2mvtJHOZeb-7ND'; //Cloud Messaging API 서버 키
$url = 'https://fcm.googleapis.com/fcm/send';

$fields = array(
		'registration_ids' => $users,
		'data' => array(
				'body' => '내용입니다...',
				'title' => '제목입니다..',
		)
);
	
$headers = array(
		'Authorization:key =' . $fcmKey,
		'Content-Type: application/json'
);

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
$result = curl_exec($ch);
if($result === FALSE) {
	die('Curl failed: ' . curl_error($ch));
}
curl_close($ch);
?>