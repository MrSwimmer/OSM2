package pro.bignerdranch.android.osmpro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bignerdranch.android.osmpro.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by Севастьян on 04.03.2017.
 */

public class NotePagerActivity extends AppCompatActivity {
    private static final String EXTRA_NOTE_ID = "com.bignerdranch.android.OSMpro.crime_id";
    private ViewPager mViewPager;
    private List<Note> mNotes;

    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, NotePagerActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pager);
        UUID noteId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        mViewPager = (ViewPager) findViewById(R.id.activity_note_pager_view_pager);
        mNotes = NoteLab.get(this).getNotes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Note note = mNotes.get(position);
                return NewNoteFragment.newInstance(note.getId());
            }

            @Override
            public int getCount() {
                return mNotes.size();
            }
        });
        for (int i = 0; i < mNotes.size(); i++) {
            if (mNotes.get(i).getId().equals(noteId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
