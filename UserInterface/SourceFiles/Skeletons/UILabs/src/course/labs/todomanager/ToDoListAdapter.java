package course.labs.todomanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import course.labs.todomanager.ToDoItem.Priority;
import course.labs.todomanager.ToDoItem.Status;

public class ToDoListAdapter extends BaseAdapter {

	// List of ToDoItems
	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
	
	private final Context mContext;

	private static final String TAG = "Lab-UserInterface";

	public ToDoListAdapter(Context context) {

		mContext = context;

	}

	// Add a ToDoItem to the adapter
	// Notify observers that the data set has changed

	public void add(ToDoItem item) {

		mItems.add(item);
		notifyDataSetChanged();

	}
	
	// Clears the list adapter of all items.
	
	public void clear(){

		mItems.clear();
		notifyDataSetChanged();
	
	}

	// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of ToDoItems

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}
	
	public void deleteItem(int pos) {
		
		mItems.remove(pos);
		notifyDataSetChanged();
	}

	// Get the ID for the ToDoItem
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}

	//Create a View to display the ToDoItem 
	// at specified position in mItems

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ToDoItem toDoItem = (ToDoItem) getItem(position);

		RelativeLayout itemLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.todo_item, parent, false);

		final TextView titleView = (TextView) itemLayout.findViewById(R.id.titleView);
		titleView.setText(toDoItem.getTitle());
	
		final CheckBox statusView = (CheckBox) itemLayout.findViewById(R.id.statusCheckBox);
		statusView.setChecked(toDoItem.getStatus() == Status.DONE);
		
		statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				log("Entered onCheckedChanged()");

				if (isChecked) {
					toDoItem.setStatus(Status.DONE);
					statusView.setChecked(true);
					View layout = (View) statusView.getParent();
					updateBackgroundColor(layout, toDoItem);
				}
				else {
					toDoItem.setStatus(Status.NOTDONE);
					statusView.setChecked(false);
					View layout = (View) statusView.getParent();
					updateBackgroundColor(layout, toDoItem);
				}
			}
		});

		List<String> options = new ArrayList<String>();
		for (Priority p : Priority.values()) {
			options.add(p.toString());
		}
			
		Log.i(TAG, toDoItem.getPriority().toString());
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, options);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final Spinner priorityView = (Spinner) itemLayout.findViewById(R.id.priorityView);
		priorityView.setAdapter(adapter);
		priorityView.setSelection(options.indexOf(toDoItem.getPriority()));
		/*
		priorityView.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Log.i(TAG, "priority has been set to " + options.get(pos));
				toDoItem.setPriority(Priority.values()[pos]);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		*/		
		
		final TextView dateView = (TextView) itemLayout.findViewById(R.id.dateView);
		dateView.setText(ToDoItem.FORMAT.format(toDoItem.getDate()));

		updateBackgroundColor(itemLayout, toDoItem);
		
		// Return the View you just created
		return itemLayout;

	}
	
	private void updateBackgroundColor(View view, ToDoItem item) {
		if (null != view)
			view.setBackgroundColor(mContext.getResources().getColor(item.getBackgroundColor()));
	}
	
	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

}
