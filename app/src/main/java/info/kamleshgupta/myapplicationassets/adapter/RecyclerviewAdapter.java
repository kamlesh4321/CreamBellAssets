package info.kamleshgupta.myapplicationassets.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import info.kamleshgupta.myapplicationassets.ApprovedRemark;
import info.kamleshgupta.myapplicationassets.R;
import info.kamleshgupta.myapplicationassets.model.OrderModel;


public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.Viewholder> {
    private final LayoutInflater inflater;
    Context context;
    List<OrderModel> list;
    public RecyclerviewAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;

    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.advanced_recyclerview_row, parent, false);

        return new Viewholder(itemView);
    }

   /* @Override
    public SimpleHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = getLayoutInflater().inflate(R.layout.advanced_recyclerview_row, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int pos = recyclerView.getChildAdapterPosition(v);
                //if (pos >= 0 && pos < getItemCount()) {
                Toast.makeText(context, "abc"+pos, Toast.LENGTH_SHORT).show();
                //}
            }
        });
        return new SimpleHolder(view);
    }*/


    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        holder.partyName.setText(list.get(position).getPartyName()+"   ");
        holder.orderNo.setText(list.get(position).getOrderNo()+"   ");
        holder.orderDate.setText(""+list.get(position).getOrderDate()+"   ");
        holder.orderQty.setText(list.get(position).getOrderQty()+"   ");
        holder.despQty.setText(""+list.get(position).getDespQty()+"   ");
        holder.deliveryDate.setText(list.get(position).getDeliveryDate()+"   ");
        if(list.get(position).getIsApp().toString().equalsIgnoreCase("1"))
        {
            holder.imageApp.setBackgroundResource(R.drawable.ok);
        }
        else
        {
            holder.imageApp.setBackgroundResource(R.drawable.del);
        }
    }

    @Override
    public int getItemCount() {
        return (list!=null)?list.size():0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView partyName ;
        TextView orderNo;
        TextView orderDate;
        TextView orderQty;
        TextView despQty;
        TextView deliveryDate;
        TextView imageApp;
        public Viewholder(View itemView) {
            super(itemView);
            partyName = (TextView) itemView.findViewById(R.id.partyName);
            orderNo = (TextView) itemView.findViewById(R.id.tvOrderNo);
            orderDate = (TextView) itemView.findViewById(R.id.tvOrderDate);
            orderQty = (TextView) itemView.findViewById(R.id.tvOrderQty);
            despQty = (TextView) itemView.findViewById(R.id.tvOrderDespQtyHeading);
            deliveryDate = (TextView) itemView.findViewById(R.id.deliveryDate);
            imageApp = (TextView) itemView.findViewById(R.id.imageApp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int pos = getAdapterPosition();
                    //Toast.makeText(partyName.getContext(),list.get(pos).getIsApp(), Toast.LENGTH_SHORT).show();
                    //final GlobalClass globalVariable = (GlobalClass) context;
                    String Heading = list.get(pos).getOrdStatus();
                    if (Heading.equalsIgnoreCase("1") || Heading.equalsIgnoreCase("0"))
                    {
                        Intent i = new Intent(context, ApprovedRemark.class);
                        i.putExtra("OrderNo", list.get(pos).getOrdNo());
                        i.putExtra("Status", list.get(pos).getIsApp());
                        context.startActivity(i);
                    }
                }
            });
        }



    }



}

