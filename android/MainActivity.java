public class MainActivity extends AppCompatActivity {
    
    private int currentAppVersionCode;
    private int latestAppVersionCode;
    private AlertDialog.Builder updateAlertDialogBuilder;
    
    // onCreate 메서드에서 현재 앱 버전 코드를 가져옵니다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentAppVersionCode = BuildConfig.VERSION_CODE; // BuildConfig.VERSION_CODE 는 현재 앱의 버전 코드를 가져옵니다.
        // ...
    }

    // onResume 메서드에서 앱 버전을 확인하고 업데이트 확인 다이얼로그를 표시합니다.
    @Override
    protected void onResume() {
        super.onResume();

        // 서버 또는 API를 사용하여 최신 앱 버전 코드를 가져옵니다.
        // 이 예제에서는 latestAppVersionCode 변수를 2로 설정했다고 가정합니다.
        latestAppVersionCode = 2;

        // 현재 앱 버전이 최신 버전이 아니라면 업데이트 확인 다이얼로그를 표시합니다.
        if (currentAppVersionCode < latestAppVersionCode) {
            showUpdateAlertDialog();
        }
    }

    // 업데이트 확인 다이얼로그를 표시합니다.
    private void showUpdateAlertDialog() {
        updateAlertDialogBuilder = new AlertDialog.Builder(this);
        updateAlertDialogBuilder.setTitle("앱 업데이트");
        updateAlertDialogBuilder.setMessage("앱 업데이트가 필요합니다. 업데이트를 진행해주세요.");
        updateAlertDialogBuilder.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Google Play 스토어에서 앱 업데이트 페이지로 이동합니다.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                startActivity(intent);
            }
        });
        updateAlertDialogBuilder.setCancelable(false);
        updateAlertDialogBuilder.show();
    }
}