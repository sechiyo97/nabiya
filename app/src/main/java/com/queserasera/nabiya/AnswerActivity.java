package com.queserasera.nabiya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;

public class AnswerActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler{
    final static int MAX_CHUR_NUM = 3;

    private BillingProcessor bp; //결제용 객체
    private SharedPreferences appData;
    private String licenseKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsq8i1gUqNuDgFRC6bmzC1RjNYSNpYUi" +
                    "MVc90D4f3wr65x/6/799CdgPy2GF88V0oyDFsdJsS8VHKi7ompgYSaI7GevNpCW42BxAe" +
                    "cNM0Iw5VAS6dBGcdVgkynqo4wF5HEz7bEugU/jNPpiZWgfVdu56KLbHbYXTgUyDe4oitlyZn" +
                    "ezPo0qAWE4LZkDsBn8GXgMqHI86Y2EqTZN3/vivH0aDUHGrsfcTMn0e8dFEEIoL1LxnVwB" +
                    "0XEpR9e7vfcb3y5r9jjIyUm+KvnZApYfibIsg3ffu/mLKFbnmTLcRWltMFq7lzLCqmgqC" +
                    "qdtXRcJ3wLBtWTr2+F5KaqcRCVVWP8wIDAQAB";
    private int numBoughtChur;

    int index = (int) (Math.random() * 100)%32;

    public static final int ran[] = {
            R.drawable.answer_navi_001, R.drawable.answer_navi_002, R.drawable.answer_navi_003, R.drawable.answer_navi_004,
            R.drawable.answer_navi_005, R.drawable.answer_navi_006, R.drawable.answer_navi_007, R.drawable.answer_navi_008,
            R.drawable.answer_navi_009, R.drawable.answer_navi_010, R.drawable.answer_navi_011, R.drawable.answer_navi_012,
            R.drawable.answer_navi_013, R.drawable.answer_navi_014, R.drawable.answer_navi_015, R.drawable.answer_navi_016,
            R.drawable.answer_navi_017, R.drawable.answer_navi_018, R.drawable.answer_navi_019, R.drawable.answer_navi_020,
            R.drawable.answer_navi_021, R.drawable.answer_navi_022, R.drawable.answer_navi_023, R.drawable.answer_navi_024,
            R.drawable.answer_navi_025, R.drawable.answer_navi_026, R.drawable.answer_navi_027, R.drawable.answer_navi_028,
            R.drawable.answer_navi_029, R.drawable.answer_navi_030, R.drawable.answer_navi_031, R.drawable.answer_navi_032
    };
    int res = ran[index];

    private ImageButton returnButton;
    private ImageButton buyChurButton;
    private boolean isMaxImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answer);

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        numBoughtChur = appData.getInt("num_bought_chur", 0);
        isMaxImg = numBoughtChur >= MAX_CHUR_NUM;

        bp = new BillingProcessor(this, licenseKey, this);
        bp.initialize();

        ImageView answerNavi = (ImageView)findViewById(R.id.answer_navi);
        answerNavi.setImageResource(res);

        returnButton = (ImageButton)findViewById(R.id.return_button);
        buyChurButton = (ImageButton)findViewById(R.id.buy_chur_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnButton.setEnabled(false); // 클릭 무효화
                Handler hButton = new Handler();
                hButton.postDelayed(new returnBtnHandler(), 600);//0.6초 지연시킨다..
                showMain(v);
            }
        });
        buyChurButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyChurButton.setEnabled(false); // 클릭 무효화
                Handler hButton = new Handler();
                hButton.postDelayed(new churBtnHandler(), 600);//0.6초 지연시킨다..
                buyChurAlert(isMaxImg);
            }
        }));
    }
    public void showMain(View v){
        finish();
    }
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(0,0);
    }
    // 여기서부터 결제 관련
    public void buyChurAlert(boolean isMaxImg){
        String alertMsg = "나비에게 츄르를 선물하시겠어요? :D";
        alertMsg += (isMaxImg)? "\n(더이상 이미지는 변하지 않습니다.)":"\n메인 이미지가 (아주 조금) 바뀝니다!";
        new AlertDialog.Builder(this)
                .setTitle("★ 나비에게 츄르 사주기 ★")
                .setMessage(alertMsg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        buyChur();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }})
                .show();
    }
    public void buyChur(){
        bp.purchase(this, "nabi_chur");
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        // * 구매 완료시 호출
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        // 츄르 구매개수 증가
        SharedPreferences.Editor editor = appData.edit();
        numBoughtChur += 1;
        editor.putInt("num_bought_chur", numBoughtChur);
        editor.apply();
        isMaxImg = numBoughtChur >= MAX_CHUR_NUM;

        // details: 결제 관련 정보
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("나비가 츄르를 맛있게 먹었습니다! >ㅁ<")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // to do action
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        bp.consumePurchase("nabi_chur");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // onActivityResult 부분이 없을시 구글 인앱 결제창이 동시에 2개가 나타나는 현상이 발생
        if (bp.handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }
    @Override
    public void onPurchaseHistoryRestored() {
        // * 구매 정보가 복원되었을때 호출
        // bp.loadOwnedPurchasesFromGoogle() 하면 호출 가능
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        // * 구매 오류시 호출
        // errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED 일때는
        // 사용자가 단순히 구매 창을 닫은것임으로 이것 제외하고 핸들링하기.
        if (errorCode != Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            String errorMessage = "구매 에러 발생 " + " (Code " + errorCode + ")";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBillingInitialized() {
        // * 처음에 초기화됬을때.
        SkuDetails mProduct = bp.getPurchaseListingDetails("nabi_chur");
        if(mProduct != null) {
            String temp = mProduct.productId + " / " + mProduct.priceText + " / "
                    + mProduct.priceValue + " / " + mProduct.priceLong;
            Log.d("BILL_INITIALIZE_SUCCESS", temp);
        } else {
            Log.d("BILL_INITIZAILZE_FAIL", "mProduct is null.");
        }
    }
    class returnBtnHandler implements Runnable{
        public void run() {
            returnButton.setEnabled(true);
        }
    }
    class churBtnHandler implements Runnable{
        public void run() {
            buyChurButton.setEnabled(true);
        }
    }
}
