package mrj.example.flags.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mrj.example.flags.R;
import mrj.example.flags.model.FlickrModel;

/**
 * Created by JavohirAI
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private Context mContext;
    private List<FlickrModel> mFlagList;

    public MainAdapter(Context MContext, List<FlickrModel> flagList) {
        mContext = MContext;
        mFlagList = flagList;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new MainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        if (holder.name_textview != null) {
            holder.name_textview.setText(mFlagList.get(position).getName());
            holder.mImageView.setImageBitmap(mFlagList.get(position).getBitmap());
            holder.itempos = position;
        }
    }

    @Override
    public int getItemCount() {
        return mFlagList.size();
    }

    class MainHolder extends RecyclerView.ViewHolder {

        public TextView name_textview;
        public ImageView mImageView;
        public Integer itempos;
        public MainHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View viewDialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_item,null,false);
                    ImageView mImageView = viewDialog.findViewById(R.id.dialog_image);
                    TextView mTxtView = viewDialog.findViewById(R.id.dialog_media_link);
                    FlickrModel mflag = mFlagList.get(itempos.intValue());
                    mImageView.setImageBitmap(mflag.getBitmap());
                    mTxtView.setText(mContext.getResources().getString(R.string.dialog_item_link,mflag.getLink()));
                    new AlertDialog.Builder(mContext)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setView(viewDialog)
                            .create()
                            .show();
                }
            });
            name_textview = itemView.findViewById(R.id.name_txt);
            mImageView = itemView.findViewById(R.id.image_view);
        }
    }
}
