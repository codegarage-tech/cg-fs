package com.rc.foodsignal.model.realm;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.rc.foodsignal.application.FoodSignalApplication;
import com.reversecoder.library.storage.SessionManager;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.rc.foodsignal.util.AllConstants.SESSION_SELECTED_CARD;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RealmController {

    private static String TAG = RealmController.class.getSimpleName();
    private static RealmController instance;
    private final Realm realm;
    private onRealmDataChangeListener onRealmDataChangeListener = null;

    public interface onRealmDataChangeListener<T extends RealmObject> {
        public void onInsert(T realmModel);

        public void onUpdate(T realmModel);

        public void onDelete(T realmModel);
    }

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Context context) {
        if (instance == null) {
            instance = new RealmController((Application) context.getApplicationContext());
        }
        return instance;
    }

    public static RealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public RealmController.onRealmDataChangeListener getOnRealmDataChangeListener() {
        return onRealmDataChangeListener;
    }

    public void setOnRealmDataChangeListener(RealmController.onRealmDataChangeListener onRealmDataChangeListener) {
        this.onRealmDataChangeListener = onRealmDataChangeListener;
    }

    public Realm getRealm() {
        return realm;
    }

    public void refresh() {
        realm.refresh();
    }

    public void destroyRealm() {
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    /***************
     * Tag handler *
     ***************/
    public void clearAllCards() {
        realm.beginTransaction();
        realm.delete(StripeCard.class);
        realm.commitTransaction();
    }

    public List<StripeCard> getCards() {
        return realm.copyFromRealm(getResultCards());
    }

    public RealmResults<StripeCard> getResultCards() {
        return realm.where(StripeCard.class).findAll();
    }

    public StripeCard getCard(StripeCard card) {
        return realm.where(StripeCard.class).equalTo("cardNumber", card.getCardNumber()).findFirst();
    }

    public boolean isCardExist(StripeCard card) {
        if (realm.where(StripeCard.class).equalTo("cardNumber", card.getCardNumber()).findFirst() != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasCards() {
        return realm.where(StripeCard.class).findAll().size() > 0;
    }

    public boolean setCard(StripeCard stripeCard) {
        if (!isCardExist(stripeCard)) {
            Log.d(TAG, stripeCard.getCardName() + " is not exist.");
            getRealm().beginTransaction();
            getRealm().copyToRealm(stripeCard);
            getRealm().commitTransaction();
        }

        if (isCardExist(stripeCard)) {
            Log.d(TAG, "Card exist: " + stripeCard.toString());

            if (onRealmDataChangeListener != null) {
                onRealmDataChangeListener.onInsert(stripeCard);
                Log.d(TAG, "Card is listening: " + stripeCard.toString());
            }

            //Save tags into session
            SessionManager.setStringSetting(FoodSignalApplication.getGlobalContext(), SESSION_SELECTED_CARD, StripeCard.getResponseString(stripeCard));

            return true;
        }

        return false;
    }

//    public RealmResults<StripeCard> queryedCards() {
//        return realm.where(StripeCard.class)
//                .contains("author", "Author 0")
//                .or()
//                .contains("title", "Realm")
//                .findAll();
//    }

//    public boolean setTags() {
//        ArrayList<StripeCard> tags = new ArrayList<>();
//        StripeCard mTag;
//        for (TagType tagType : TagType.values()) {
//            Log.d(TAG, "tag is: " + tagType.name());
//            mTag = new StripeCard(tagType.name());
//            if (!isTagExist(mTag)) {
//                Log.d(TAG, tagType.name() + " is not exist.");
//                getRealm().beginTransaction();
//                getRealm().copyToRealm(mTag);
//                getRealm().commitTransaction();
//            }
//
//            if (isTagExist(mTag)) {
//                Log.d(TAG, "Tag exist: " + mTag.toString());
//                tags.add(mTag);
//
//                if (onRealmDataChangeListener != null) {
//                    onRealmDataChangeListener.onInsert(mTag);
//                    Log.d(TAG, "Tag is listening: " + mTag.toString());
//                }
//            }
//        }
//
//        //Save tags into session
//        DataTag dataTag = new DataTag(tags);
//        SessionManager.setStringSetting(RecycleBinApp.getGlobalContext(), SESSION_DATA_TAGS, DataTag.convertFromObjectToString(dataTag));
//
//        return true;
//    }
}