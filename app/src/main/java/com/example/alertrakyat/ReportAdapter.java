package com.example.alertrakyat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private ArrayList<Report> reportList;
    private String userId;
    public ReportAdapter(ArrayList<Report> reportList, String userId) {
        this.reportList = reportList;
        this.userId = userId; // Initialize userId
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);

        // Set the dynamic report number (e.g., Report 1, Report 2, etc.)
        holder.reportNumber.setText("Report " + (position + 1));

        holder.reportName.setText(report.getName());
        holder.reportDate.setText(report.getDate());
        holder.reportDescription.setText(report.getDescription());

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            String reportId = report.getId();  // Assuming you add an ID field in Report model
            Context context = v.getContext();  // Get the context from the view
            deleteReport(context, reportId); // Pass context to deleteReport
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    // Delete report from Firebase
    private void deleteReport(Context context, String reportId) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("reports").child(userId);
        database.child(reportId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Notify the adapter about the deletion
                        Toast.makeText(context, "Report deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to delete report", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView reportName, reportDate, reportDescription,reportNumber;
        Button deleteButton;

        public ReportViewHolder(View itemView) {
            super(itemView);
            reportNumber = itemView.findViewById(R.id.report_number);
            reportName = itemView.findViewById(R.id.report_name);
            reportDate = itemView.findViewById(R.id.report_date);
            reportDescription = itemView.findViewById(R.id.report_description);
            deleteButton = itemView.findViewById(R.id.delete_report_button);

        }
    }
}