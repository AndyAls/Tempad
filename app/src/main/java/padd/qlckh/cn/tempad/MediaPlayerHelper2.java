package padd.qlckh.cn.tempad;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RawRes;

import io.reactivex.CompletableEmitter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Andy
 * @date   2021/11/5 17:40
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    MediaPlayerHelper.java
 */
public class MediaPlayerHelper2 {

    private Context mContext;
    private final AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private boolean isFouce;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Disposable subscribe;
    private static volatile MediaPlayerHelper2 playerHelper = null;

    private MediaPlayerHelper2(Context context) {
        this.mContext = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(false);
    }


    public static MediaPlayerHelper2 getInstance(Context context) {
        if (playerHelper == null) {
            synchronized (MediaPlayerHelper2.class) {
                if (playerHelper == null) {
                    playerHelper = new MediaPlayerHelper2(context);
                }
            }
        }
        return playerHelper;
    }

    public void startPlay(@RawRes final int rawId) {
        try {
            preparePlay(null, rawId);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        subscribe = Completable.create(new CompletableOnSubscribe() {
//            @Override
//            public void subscribe(final CompletableEmitter emitter) throws Exception {
//                preparePlay(emitter, rawId);
//            }
//        })
//                .subscribeOn(Schedulers.single())
//                .subscribe(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        stopPlay();
//                    }
//                });


    }

    public void setProgress(int progress) {
        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.reset();
//            mediaPlayer.start();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer.seekTo(progress, MediaPlayer.SEEK_CLOSEST);
            } else {
                mediaPlayer.seekTo(progress);
            }
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


    private void preparePlay(final CompletableEmitter emitter, @RawRes int rawId) throws Exception {
        if (mediaPlayer == null || mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlay();
            }

        });
        AssetFileDescriptor file = mContext.getResources().openRawResourceFd(
                rawId);
        mediaPlayer.setDataSource(file.getFileDescriptor(),
                file.getStartOffset(), file.getLength());
        file.close();
        mediaPlayer.prepareAsync();
//        mediaPlayer.start();
//        Thread.sleep(1000);
    }


    public void onResume() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }


    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void onTogle() {
        if (mediaPlayer.isPlaying()) {
            onPause();
        } else {
            onResume();
        }
    }

    public void onPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (playerHelper != null) {
            playerHelper = null;
        }
        if (mContext != null) {
            mContext = null;
        }
        compositeDisposable.clear();
        /*if (!subscribe.isDisposed()) {
            subscribe.dispose();
        }*/


    }


    private void stopPlay() {
        mediaPlayer.reset();
    }

}
