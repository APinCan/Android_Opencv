 package com.example.textrecognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

 public class MainActivity extends AppCompatActivity {
    private final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE=1001;
    private static final int PICK_FROM_ALBUM = 2000;
    Button buttonload, buttonconvert;
    ImageView topImageView,bottomImageView;
    Bitmap originalImage;
     //tessract사용을 위한 관련 클래스 객체를 생성, TessBaseAPI클래스 객체를 위한 참조변수를 정의
//    TessBaseAPI tessBaseAPI;
     /*
    https://m.blog.naver.com/cosmosjs/220937785735
    tessract 사용 예
     */
     Bitmap image; //사용되는이미지
     private TessBaseAPI mTess; //Tess Api reference
     String datapath=""; //언어데이터가 있는 경로

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        OpenCVLoader.initDebug();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!OpenCVLoader.initDebug()){
            //handle initilaizaiton error
        }


        topImageView=(ImageView)findViewById(R.id.topImageView);
       // bottomImageView=(ImageView)findViewById(R.id.bottonImageView2);
        final TextView textView=(TextView)findViewById(R.id.OCRTextView);

        checkPermission();

        final TesseractOCR tesseractOCR = new TesseractOCR(this, "eng");
        buttonload =(Button)findViewById(R.id.btnload);
        buttonload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbum();
            }
        });
        buttonconvert=(Button)findViewById(R.id.btnconvert);
        buttonconvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imagePreProcessing
                String text=tesseractOCR.getOCRResult(originalImage);
                textView.setText(text);
             //   new AsyncTess().execute(originalImage);
            }
        });


        //tessract ocr에 적용할 언어를 지정, 언어관련파일은 사전에 assets폴더나 외장 스토리지에 저장해야 사용가능
//        tessBaseAPI = new TessBaseAPI();
//        String dir = getFilesDir()+"/tessract";
//        if(checkLanguageFile(dir+"/tessdata"))
//            tessBaseAPI.init(dir, "kor");
       // useTessract();
//        tessBaseAPI = new TessBaseAPI();
//        String dir = getFilesDir()+"/tessract";
     //   if(checkLanguageFile(dir+"/tessdata"))
     //       tessBaseAPI.init(dir, "kor");
        /*
    https://m.blog.naver.com/cosmosjs/220937785735
    tessract 사용 예
     */
        //이미지 디코딩을 위한 초기화는 origianl image가 있으므로 패쓰
        //image=BitmapFactory.decodeResource(getResources(), R.drawable.~~);
        //언어파일 경로
        //datapath=getFilesDir()+"/tesseract/";
//        datapath=Environment.getExternalStorageDirectory()+"/TextRecognition/";
//        //트레이닝데이터가 카피되어있늖지 확인
//        checkFile(new File(datapath+"tessdata/"));
//        //Tesseract API
//        String lang = "eng";



//        mTess=new TessBaseAPI();
//        mTess.init(datapath, lang);
    }


    /*
    https://m.blog.naver.com/cosmosjs/220937785735
    tessract 사용 예
     */
    //process an Image
     public void processImage(View view){
         String OCResult = null;
         mTess.setImage(originalImage);
         OCResult=mTess.getUTF8Text();

         TextView OCRTextView = (TextView)findViewById(R.id.OCRTextView);
         OCRTextView.setText(OCResult);
     }
     /*
https://m.blog.naver.com/cosmosjs/220937785735
tessract 사용 예
*/
     //copy file to device
     private void copyFiles(){
         try{
             String filepath = datapath+"/tessdata/eng.traineddata";
             AssetManager assetManager=getAssets();
             InputStream inputStream=assetManager.open("tessdata/eng.traineddata");
             OutputStream outputStream = new FileOutputStream(filepath);
             byte []buffer = new byte[1024];
             int read;
             while((read=inputStream.read(buffer))!=-1){
                 outputStream.write(buffer,0,read);
             }
             outputStream.flush();
             outputStream.close();
             inputStream.close();

         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     /*
https://m.blog.naver.com/cosmosjs/220937785735
tessract 사용 예
*/
     //check file on the device
     private void checkFile(File dir){
        //디렉토리가 없으면 디렉토리를 만들고 그 후에 파일을 카피
         if(!dir.exists() && dir.mkdir()){
             copyFiles();
         }
         //디렉토리가 있지만 파일이 없으면 파일카피 진행
         if(dir.exists()){
             String datafilepath=datapath+"/tessdata/eng.traineddata";
             File datafile = new File(datafilepath);
             if(!datafile.exists()){
                 copyFiles();
             }
         }
     }




//    private void useTessract(){
//        tessBaseAPI = new TessBaseAPI();
//        String dir = getFilesDir()+"/tessract";
//        if(checkLanguageFile(dir+"/tessdata"))
//            tessBaseAPI.init(dir, "kor");
//
//        new AsyncTess().execute(originalImage);
//    }

    //tessract 연관파일
     //Assets폴더에 미리 언어파일을 저장하고, 처음 프로그램이 실행되면, 내부 디렉토리에 파일을 복사하여 사용
     //이 경우 각 기기마다 별도로 언어파일 외장 스토리지에 저장 필요 X

     //내부디렉토리에 해당파일(언어파일)이 존재하는지 체크, 업을 경우 createFiles()함수에서
     //Assets폴더 내 언어 데이터파일을 읽어 지정된 경로에 파일을 복사
//   boolean checkLanguageFile(String dir){
//        File file=new File(dir);
//
//        if(!file.exists() && file.mkdir()){
//            createFiles(dir);
//        }
//        else if(file.exists()){
//            String filePath=dir+"/kor.traineddata";
//            File langDataFile=new File(filePath);
//
//            if(!langDataFile.exists()){
//                createFiles(dir);
//            }
//        }
//        return true;
//    }
//
//    //checkLanguageFile과 세트
//    private void createFiles(String dir){
//        AssetManager assetManager = this.getAssets();
//        InputStream inputStream = null;
//        OutputStream outputStream=null;
//
//        try{
//            inputStream=assetManager.open("kor.traineddata");
//            String destFile=dir+"/kor.traineddata";
//            outputStream=new FileOutputStream(destFile);
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while((read=inputStream.read(buffer)) !=-1){
//                outputStream.write(buffer, 0, read);
//            }
//
//            inputStream.close();
//            outputStream.flush();
//            outputStream.close();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//    }

    //testBaseApi 클래스 객체의 setImage함수의 인자로 이미지를 설정하고 getUTF를 통해 텍스트 추출결과 얻음
     //실질적으로 tessBAseApi.setImage와 tesBaseAPI.getUTF8text 두개의 함수  호출로 이미지로부터
     //텍스트를 인식/추출
//     private class AsyncTess extends AsyncTask<Bitmap, Integer, String >{
//        @Override
//        protected String doInBackground(Bitmap... mRelativeParams){
//        //protected String doInBackground(Bitmap... bitmaps) {
//            tessBaseAPI.setImage(mRelativeParams[0]);
//
//            return tessBaseAPI.getUTF8Text();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Toast.makeText(MainActivity.this, ""+result, Toast.LENGTH_LONG).show();
//        }
//    }

    //왜 있는건지 생각필요
//    public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees){
//        if(degrees !=0 && bitmap !=null){
//            Matrix m = new Matrix();
//            m.setRotate(degrees, (float)bitmap.getWidth()/2, (float)bitmap.getHeight()/2);
//            try {
//                Bitmap b2= Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
//
//                if(bitmap!=b2){
//                    bitmap=b2;
//                }
//            } catch (OutOfMemoryError ex){
//                ex.printStackTrace();
//            }
//        }
//
//        return bitmap;
//    }

    public void checkPermission(){
        //퍼미션을 체크 권한이 있는경우 PackageManager.PERMISSION_GRANTED반환
        //권한이 없는 경우 PackageManager.PERMISSION_DENIED반환
//        int  permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int  permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //checkSelfPermission() : 권한 부여 여부
        //requestPermission() L 권한 요청
        //onRequestPermsiionReuslt() : 권한요청 콜백
        //shouldShowRequestPermissonRationale() : 사용자에게 설며이 필요한 상황 확인
//        int permissionArray[] = {
//                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE),
//                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA),
//        };

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 설정이 필요합니다", Toast.LENGTH_SHORT).show();
            //사용자에게 권한 요청할때 그 권한이 필요한 이유 사용자에게 설명
            //설명해 주기 위해 shouldShowRequestPermissionRationale 메서드 사용
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "저장을 위한 권한 설정이 필요합니다", Toast.LENGTH_SHORT).show();
            }
            //앱에 권한이 없는경우 reqeustPermissions 메서드를 호출해 권한 요청 후 사용자가 응답한 결과를 콜백
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                Toast.makeText(this, "저장을 위한 권한 설정이 필요합니다2", Toast.LENGTH_SHORT).show();
            }
        }

//        for(int i=0;i<permissionArray.length;i++) {
//            if (permissionArray[i] != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "권한 설정이 필요합니다", Toast.LENGTH_SHORT).show();
//                //사용자에게 권한 요청할때 그 권한이 필요한 이유 사용자에게 설명
//                //설명해 주기 위해 shouldShowRequestPermissionRationale 메서드 사용
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    Toast.makeText(this, "저장을 위한 권한 설정이 필요합니다", Toast.LENGTH_SHORT).show();
//                }
//                //앱에 권한이 없는경우 reqeustPermissions 메서드를 호출해 권한 요청 후 사용자가 응답한 결과를 콜백
//                else {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
//                    Toast.makeText(this, "저장을 위한 권한 설정이 필요합니다2", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
    }

    //사용자가 응답하면 시스템은 onRequestPermsiionReuslt()메서드 호출해 사용자 응답을 전달
     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "승인 허가",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "승인 허가받지 않았음", Toast.LENGTH_LONG).show();
                }
        }

        return ;
     }

     //앨범에서 선택한 이미지를 가져옴
     private void goToAlbum(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_FROM_ALBUM){
            if(resultCode==RESULT_OK){
                try{
                    //선택한 이미지에서 비트맵 생성
                    InputStream in=getContentResolver().openInputStream(data.getData());

                    originalImage = BitmapFactory.decodeStream(in);
                    in.close();

                    //이미지표시
                    topImageView.setImageBitmap(originalImage);

                     /*
                    이미지 회전 에러 적용코드
                     */

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
     }

     //grayscale과 threshold수행
     public void imagePreProcessing(){
         //Mat를 하나 만들고 비트맵을 mat로 변환
         Mat img1=new Mat();
         Utils.bitmapToMat(originalImage, img1);

         //그레이스캐일과 이진화를 위한  Mat생성
         Mat imageGray1 = new Mat();
         Mat imageCny1 = new Mat();

         //그레이스케일로 전환
         Imgproc.cvtColor(img1, imageGray1, Imgproc.COLOR_BGR2GRAY);
         //이진화 시킴
         Imgproc.threshold(imageGray1, imageCny1, 160,255, Imgproc.THRESH_BINARY);

         //Mat 배열의 imageCny1에서 cols와 rows를 뽑아와 bitmap을 만들기
         Bitmap tmpBitmap = Bitmap.createBitmap(imageCny1.cols(), imageCny1.rows(), Bitmap.Config.ARGB_8888);
         //여기서는 안드로이드는 RGB를 쓰고 opencv는 BGR을 쓰기때문에 BGR을 안드로이드의 RGB로 고쳐주는것
         //Imgproc.cvtColor(imageCny1,imageCny1, Imgproc.COLOR_BGR2RGBA);
         //이진화 이미지를 비트맵으로
         Utils.matToBitmap(imageCny1, tmpBitmap);

         bottomImageView.setImageBitmap(tmpBitmap);

     }

     //관심영역 추출하기
    public void extractROI(Mat imageCny){
        List<MatOfPoint> contours=new ArrayList<>();
        Mat hierarachy = new Mat();
        //hierararchy가 왜 ㅣㅍㄹ요한지 아래 코드가 무엇인지 알아보는것필요
        Imgproc.findContours(imageCny, contours, hierarachy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for(int i=0;i>=0;i=(int)hierarachy.get(0, i)[0]){
            MatOfPoint matOfPoint=contours.get(i);

            Rect rect=Imgproc.boundingRect(matOfPoint);
            //사각형의 크기에 따라 출력여부 결정
            if(rect.width<30 || rect.height<30 || rect.width <=rect.height || rect.x<20 || rect.y<20
            || rect.width<= rect.height*3 || rect.width >= rect.height*6) continue;

            //Roi출력
            Bitmap roi = Bitmap.createBitmap(originalImage, (int)rect.tl().x, (int)rect.tl().y, rect.width, rect.height);

        }
    }
 }
