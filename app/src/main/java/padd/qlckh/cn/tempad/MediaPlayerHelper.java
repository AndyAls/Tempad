package padd.qlckh.cn.tempad;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RawRes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableEmitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Andy
 * @date   2021/11/5 17:40
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    MediaPlayerHelper.java
 */
public class MediaPlayerHelper {

    private Context mContext;
    private final AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private final AudioManager.OnAudioFocusChangeListener mListener;
    private boolean isFouce;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Disposable subscribe;
    private static volatile MediaPlayerHelper playerHelper = null;

    private MediaPlayerHelper(Context context) {
        this.mContext = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(false);
        //音频焦点监听,录音时应用获取焦点,暂停系统音乐播放
        mListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {

                    case AudioManager.AUDIOFOCUS_GAIN:
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                        isFouce = true;
                        requestAudioFocus();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        onPause();
                        isFouce = false;
                        abandonAudioFocus();
                        break;
                    default:
                }
            }
        };
    }


    public static MediaPlayerHelper getInstance(Context context) {
        if (playerHelper == null) {
            synchronized (MediaPlayerHelper.class) {
                if (playerHelper == null) {
                    playerHelper = new MediaPlayerHelper(context);
                }
            }
        }
        return playerHelper;
    }


    public void startPlay(@RawRes final int rawId) {
        requestAudioFocus();
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
        abandonAudioFocus();
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

    private void requestAudioFocus() {
        if (!isFouce) {
            int result = audioManager.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                isFouce = true;
            }
        }
    }

    private void abandonAudioFocus() {
        if (isFouce) {
            audioManager.abandonAudioFocus(mListener);
            isFouce = false;
        }
    }

    private void stopPlay() {
        mediaPlayer.reset();
        abandonAudioFocus();
    }

}
