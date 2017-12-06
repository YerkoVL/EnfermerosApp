package com.yarolegovich.slidingrootnav.sample.fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.yarolegovich.slidingrootnav.sample.R;

import static android.app.Activity.RESULT_OK;

public class PagoFragment extends Fragment{

    private final int CREAR_NUEVA_TARJETA = 0;

    private LinearLayout contenedorTarjetas;
    private Button agregarTarjeta;

    private Context ctx = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ctx = getActivity().getApplicationContext();

        inicializar();
        escuchadores();
        return inflater.inflate(R.layout.pago_fragment_layout, container, false);
    }

    public void inicializar(){
        poblar();
    }

    public void poblar(){
        CreditCardView sampleCreditCardView = new CreditCardView(ctx);

        String name = "Yerko Vera";
        String cvv = "420";
        String expiry = "01/18";
        String cardNumber = "4242424242424242";

        sampleCreditCardView.setCVV(cvv);
        sampleCreditCardView.setCardHolderName(name);
        sampleCreditCardView.setCardExpiry(expiry);
        sampleCreditCardView.setCardNumber(cardNumber);

        contenedorTarjetas.addView(sampleCreditCardView);
        int index = contenedorTarjetas.getChildCount() - 1;
        agregarTarjeta(index, sampleCreditCardView);
    }

    private void agregarTarjeta(final int index, CreditCardView creditCardView) {
        creditCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreditCardView creditCardView = (CreditCardView) v;
                String cardNumber = creditCardView.getCardNumber();
                String expiry = creditCardView.getExpiry();
                String cardHolderName = creditCardView.getCardHolderName();
                String cvv = creditCardView.getCVV();

                Intent intent = new Intent(ctx, CardEditActivity.class);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, cardHolderName);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_NUMBER, cardNumber);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_EXPIRY, expiry);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_SHOW_CARD_SIDE, CreditCardUtils.CARD_SIDE_BACK);
                intent.putExtra(CreditCardUtils.EXTRA_VALIDATE_EXPIRY_DATE, false);

                // start at the CVV activity to edit it as it is not being passed
                intent.putExtra(CreditCardUtils.EXTRA_ENTRY_START_PAGE, CreditCardUtils.CARD_CVV_PAGE);
                startActivityForResult(intent, index);
            }
        });
    }

    public void escuchadores(){
        agregarTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, CardEditActivity.class);
                startActivityForResult(intent, CREAR_NUEVA_TARJETA);
            }
        });
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
//            Debug.printToast("Result Code is OK", getApplicationContext());

            String name = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);

            if (reqCode == CREAR_NUEVA_TARJETA) {

                CreditCardView creditCardView = new CreditCardView(ctx);

                creditCardView.setCVV(cvv);
                creditCardView.setCardHolderName(name);
                creditCardView.setCardExpiry(expiry);
                creditCardView.setCardNumber(cardNumber);

                contenedorTarjetas.addView(creditCardView);
                int index = contenedorTarjetas.getChildCount() - 1;
                agregarTarjeta(index, creditCardView);

            } else {

                CreditCardView creditCardView = (CreditCardView) contenedorTarjetas.getChildAt(reqCode);

                creditCardView.setCardExpiry(expiry);
                creditCardView.setCardNumber(cardNumber);
                creditCardView.setCardHolderName(name);
                creditCardView.setCVV(cvv);

            }
        }

    }

}
