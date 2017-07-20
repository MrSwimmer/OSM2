package pro.bignerdranch.android.osmpro;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.osmpro.R;

import pro.bignerdranch.android.osmpro.puls.Pulsometro;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Севастьян on 01.03.2017.
 */

public class NewNoteFragment extends Fragment {
    private static final String TAG = "QQQ";
    private final int DIALOG = 1;
    private static final String ARG_NOTE_ID = "note_id";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "DialogDate";
    private Note mNote;
    private boolean Neuro=false;
    private LinearLayout InfoZone;
    private LinearLayout Chss1, Chss2;
    private EditText mPsit;
    private Boolean sec = true;
    private EditText mPstand;
    String p1 = null, p2 = null;
    private TextView mResView;
    private ScrollView mScrollView;
    private EditText mRec;
    private Button mButton;
    private TextView mSaveAll;
    private TextView mText16;
    private Button mButtonDate;
    private TextView Zone;
    private TextView AboutZone;
    private RadioGroup mRadioGroup;
    private LinearLayout MainBottom;
    private RadioButton mRadioButtonMorning;
    private RadioButton mRadioButtonTrain;
    private Chronometer mChronometer;
    private ImageView mButtonStart;
    private ImageView mButtonReset;
    private ImageView mChrono;
    private ImageView mZoneBallView;
    private Boolean actVib = false;
    private Button scanPuls;
    private ImageView Arrow;
    AlertDialog.Builder ad;
    Context context;
    public static NewNoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteId);
        NewNoteFragment fragment = new NewNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void secondCircularImageBar(ImageView iv2, int z, String s) {

        Bitmap b = Bitmap.createBitmap(450, 450, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#efefef"));
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(225, 225, 205, paint);
        canvas.drawCircle(225, 225, 175, paint);
        canvas.drawCircle(225, 225, 145, paint);
        canvas.drawCircle(225, 225, 115, paint);

        switch (z) {
            case 1:
                paint.setColor(Color.parseColor("#c0e637"));
                canvas.drawCircle(225, 225, 205, paint);
                break;
            case 2:
                paint.setColor(Color.parseColor("#55bbeb"));
                canvas.drawCircle(225, 225, 175, paint);
                break;
            case 3:
                paint.setColor(Color.parseColor("#f0d73c"));
                canvas.drawCircle(225, 225, 145, paint);
                break;
            case 4:
                paint.setColor(Color.parseColor("#e76660"));
                canvas.drawCircle(225, 225, 115, paint);
                break;
        }
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#FFC95644"));
        paint.setTextSize(60);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(s, 225, 225 + (paint.getTextSize() / 3), paint);
        iv2.setImageBitmap(b);
    }
    private void circularImageBar(ImageView iv2, int i) {

        Bitmap b = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#c4c4c4"));
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(150, 150, 140, paint);
        paint.setColor(Color.parseColor("#d74b4e"));
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.FILL);
        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);
        oval.set(10, 10, 290, 290);
        canvas.drawArc(oval, 270, ((i * 360) / 60), false, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#FFC95644"));
        paint.setTextSize(80);
        canvas.drawText("" + (i / 60) + ":" + i, 150, 150 + (paint.getTextSize() / 3), paint);
        iv2.setImageBitmap(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID noteId = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = NoteLab.get(getActivity()).getNote(noteId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.new_fragment_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                NoteLab.get(getActivity()).delNote(mNote);
                getActivity().finish();
                return true;
            case R.id.menu_item_send_note:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getNoteReport());
                i.putExtra(Intent.EXTRA_SUBJECT, "Отправка результатов");
                i = Intent.createChooser(i, "Отправить с помощью");
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onStartClick() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    public void onStopClick() {
        mChronometer.stop();
    }

    public void onResetClick() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_fragment_note, container, false);
        mChrono = (ImageView) v.findViewById(R.id.chrono);
        Chss1 = (LinearLayout) v.findViewById(R.id.chss1);
        Chss1.setBackgroundResource(R.drawable.grey_shape);
        scanPuls = (Button) v.findViewById(R.id.Scan);
        scanPuls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Pulsometro.class);
                Log.i("LOL", String.valueOf(mNote.getId()));
                i.putExtra("id", mNote.getId().toString());
                startActivity(i);
                getActivity().finish();
            }
        });
        Chss2 = (LinearLayout) v.findViewById(R.id.chss2);
        Chss2.setBackgroundResource(R.drawable.grey_shape);
        circularImageBar(mChrono, 0);
        mPsit = (EditText) v.findViewById(R.id.p_sit);
        mPsit.setText(mNote.getPsit());
        mChronometer = (Chronometer) v.findViewById(R.id.chronometer);
        mChronometer.setVisibility(View.INVISIBLE);
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime()
                        - mChronometer.getBase();
                if (elapsedMillis >= 60000)
                    elapsedMillis = 0;
                circularImageBar(mChrono, (int) (elapsedMillis / 1000));
                if (elapsedMillis > 15000 && !actVib) {
                    actVib = true;
                    Intent intentVibrate = new Intent(getActivity(), VibrateService.class);
                    getActivity().startService(intentVibrate);
                }
            }
        });
        mButtonStart = (ImageView) v.findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sec) {
                    onStartClick();
                    mButtonStart.setImageResource(R.drawable.stop);
                    sec = !sec;
                } else {
                    onStopClick();
                    mButtonStart.setImageResource(R.drawable.start);
                    sec = !sec;
                }
            }
        });

        mPsit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setPsit(s.toString());

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPstand = (EditText) v.findViewById(R.id.p_stand);
        mPstand.setText(mNote.getPstand());
        mPstand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mNote.setPstand(s.toString());

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRec = (EditText) v.findViewById(R.id.rec);
        mRec.setText(mNote.getRec());
        mRec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setRec(s.toString());

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mButtonDate = (Button) v.findViewById(R.id.note_date);
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();

                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mNote.getDate());
                dialog.setTargetFragment(NewNoteFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        mButtonDate.setText(mNote.getDateForm());
        mRadioButtonMorning = (RadioButton) v.findViewById(R.id.radio_after_sleep);
        mRadioButtonTrain = (RadioButton) v.findViewById(R.id.radio_after_train);
        mRadioButtonMorning.setChecked(mNote.isRad());
        mRadioButtonTrain.setChecked(!mNote.isRad());
        mRadioButtonMorning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNote.setRad(!mNote.isRad());
                Log.i(TAG, String.valueOf(mNote.isRad()));
            }
        });
        mZoneBallView = (ImageView) v.findViewById(R.id.resview);
        mButton = (Button) v.findViewById(R.id.button);
        mResView = (TextView) v.findViewById(R.id.restext);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.radiogroup);
        InfoZone = (LinearLayout) v.findViewById(R.id.infozone);
        Zone = (TextView) v.findViewById(R.id.textZone);
        mScrollView = (ScrollView) v.findViewById(R.id.scroll);
        MainBottom = (LinearLayout) v.findViewById(R.id.main_bottom);
        mSaveAll = (Button) v.findViewById(R.id.save);
        AboutZone = (TextView) v.findViewById(R.id.textLetZone);
        mScrollView.animate();
        Arrow = (ImageView) v.findViewById(R.id.arrow);
        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_one);
        if (mNote.getPsit()!=null&&mNote.getPstand()!=null) {
            MainBottom.setVisibility(View.VISIBLE);
            Arrow.setVisibility(View.VISIBLE);
            Arrow.startAnimation(animation);
            secondCircularImageBar(mZoneBallView, mNote.getZone(), mNote.getPoint());

            //mScrollView.scrollTo((int) mSaveAll.getX(), (int) mSaveAll.getY());
            //mScrollView.scrollTo(0, (int) mRadioButtonMorning.getY());
            switch (mNote.getZone()) {
                case 1:
                    Zone.setText(R.string.zone_1t);
                    InfoZone.setBackgroundColor(getResources().getColor(R.color.green));
                    AboutZone.setText(R.string.zone_1);
                    break;
                case 2:
                    Zone.setText(R.string.zone_2t);
                    AboutZone.setText(R.string.zone_2);
                    InfoZone.setBackgroundColor(getResources().getColor(R.color.blue));
                    break;
                case 3:
                    Zone.setText(R.string.zone_3t);
                    AboutZone.setText(R.string.zone_3);
                    InfoZone.setBackgroundColor(getResources().getColor(R.color.yellow));
                    break;
                case 4:
                    Zone.setText(R.string.zone_4t);
                    AboutZone.setText(R.string.zone_4);
                    InfoZone.setBackgroundColor(getResources().getColor(R.color.red));
                    break;
            }
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNote.getPsit() != null && mNote.getPstand() != null) {

                    MainBottom.setVisibility(View.VISIBLE);
                    Arrow.setVisibility(View.VISIBLE);
                    Arrow.startAnimation(animation);
                    secondCircularImageBar(mZoneBallView, mNote.getZone(), mNote.getPoint());

                    //mScrollView.scrollTo(0, 3000);
                    //mScrollView.scrollTo((int) mSaveAll.getX(), (int) mSaveAll.getY());
                    //mScrollView.scrollTo((int) mZoneBallView.getX(), (int) mRadioButtonMorning.getY());
                    switch (mNote.getZone()) {
                        case 1:
                            Zone.setText(R.string.zone_1t);
                            AboutZone.setText(R.string.zone_1);
                            InfoZone.setBackgroundColor(getResources().getColor(R.color.green));
                            break;
                        case 2:
                            Zone.setText(R.string.zone_2t);
                            AboutZone.setText(R.string.zone_2);
                            InfoZone.setBackgroundColor(getResources().getColor(R.color.blue));
                            break;
                        case 3:
                            Zone.setText(R.string.zone_3t);
                            AboutZone.setText(R.string.zone_3);
                            InfoZone.setBackgroundColor(getResources().getColor(R.color.yellow));
                            break;
                        case 4:
                            Zone.setText(R.string.zone_4t);
                            AboutZone.setText(R.string.zone_4);
                            InfoZone.setBackgroundColor(getResources().getColor(R.color.red));
                            break;
                    }

                }
            }
        });
        final String[] mChooseVar = { "Балл точен", "Балл меньше на <1", "Балл меньше на <2", "Балл больше на <1", "Балл больше на <2" };
        mSaveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoteSettings.SmartScore){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Оценка точности")
                            .setCancelable(false)
                            .setItems(mChooseVar,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int item) {
                                            switch (item){
                                                case 0:
                                                    dialog.cancel();
                                                    getActivity().finish();
                                                    break;
                                                case 1:
                                                    Note.x+=0.02;
                                                    Note.y-=0.02;
                                                    dialog.cancel();
                                                    getActivity().finish();
                                                    break;
                                                case 2:
                                                    Note.x+=0.04;
                                                    Note.y-=0.04;
                                                    dialog.cancel();
                                                    getActivity().finish();
                                                    break;
                                                case 3:
                                                    Note.x-=0.02;
                                                    Note.y+=0.02;
                                                    dialog.cancel();
                                                    getActivity().finish();
                                                    break;
                                                case 4:
                                                    Note.x-=0.04;
                                                    Note.y+=0.04;
                                                    dialog.cancel();
                                                    getActivity().finish();
                                                    break;
                                            }
                                            Neuro=true;
                                            Log.i("XY", Note.x+" "+Note.y);
                                            Toast.makeText(getActivity(),"Спасибо", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                    getActivity().finish();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mNote.setDate(date);
        }
    }
    @Override
    public void onStop(){
        super.onStop();
        if(mNote.getPsit()==null&&mNote.getPstand()==null){
            NoteLab.get(getActivity()).delNote(mNote);
            //getActivity().finish();
        }
    }
    @Override
    public void onResume() {
            super.onResume();
            mButtonDate.setText(mNote.getDateForm());
            if (Pulsometro.text != "") {
                int buf = 2;
                for (int i = 0; i < Pulsometro.text.length(); i++) {
                    if (Pulsometro.text.charAt(i) == ' ')
                            buf = i;
                }
                mPsit.setText(Pulsometro.text.substring(0, buf));
                mPstand.setText(Pulsometro.text.substring(buf + 1, Pulsometro.text.length()));
                Pulsometro.text="";
            }
        }
    @Override
    public void onPause() {
        super.onPause();
        NoteLab.get(getActivity())
                .updateNote(mNote);
    }
    private String getNoteReport() {
        String radioString = null;
        if (mNote.isRad()) {
            radioString = "После сна";
        } else {
            radioString = "После тренировки";
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,
                mNote.getDate()).toString();
        String recString = mNote.getRec();
        String sitString = mNote.getPsit();
        String standString = mNote.getPstand();
        String zoneString = Integer.toString(mNote.getZone());
        String pointsString = mNote.getPoint();
        String report = getString(R.string.note_report, dateString, radioString, sitString, standString, pointsString, zoneString, recString);
        return report;
    }

}

