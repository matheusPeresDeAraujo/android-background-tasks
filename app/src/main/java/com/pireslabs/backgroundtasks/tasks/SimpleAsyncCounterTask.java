package com.pireslabs.backgroundtasks.tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pireslabs.android.utils.log.Log;

import java.lang.ref.WeakReference;

public final class SimpleAsyncCounterTask extends AsyncTask<Integer, Integer, Void> {

    private static final String TAG = SimpleAsyncCounterTask.class.getSimpleName();

    private final WeakReference<TextView> textView;

    private final WeakReference<ProgressBar> progressBar;

    public SimpleAsyncCounterTask(TextView textView, ProgressBar progressBar) {
        this.textView = new WeakReference<>(textView);
        this.progressBar = new WeakReference<>(progressBar);
        Log.debug(TAG, "AsyncTask criada.");
    }

    private void changeCounterValueTo(int counterValue) {
        TextView textView = this.textView.get();
        if (textView != null) {
            textView.setText(String.valueOf(counterValue));
        }
    }

    @Override
    protected void onPreExecute() {
        Log.debug(TAG, "Executando o método onPreExecute.");
        this.progressBar.get().setVisibility(View.VISIBLE);
        this.changeCounterValueTo(0);
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        Log.debug(TAG, "Iniciando execução do método doInBackground.");
        Integer counterSize = integers[0];
        Log.info(TAG, "Minha tarefa é contar até: " + counterSize);
        for (int i = 0; i < counterSize; i++) {
            if (isCancelled())
                break;
            Log.debug(TAG, String.format("Contador: %s", i));
            publishProgress(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                changeCounterValueTo(0);
                Log.error(TAG, e.getMessage());
                return null;
            }
        }
        Log.debug(TAG, "Fim da execução do método doInBackground.");
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.debug(TAG, "Atualizando a UI através do método onProgressUpdate.");
        this.changeCounterValueTo(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.debug(TAG, "Executando o método onPostExecute.");
        this.progressBar.get().setVisibility(View.INVISIBLE);
        Toast.makeText(this.progressBar.get().getContext(), "Contagem concluída.", Toast.LENGTH_SHORT).show();
        this.changeCounterValueTo(0);
    }
}
