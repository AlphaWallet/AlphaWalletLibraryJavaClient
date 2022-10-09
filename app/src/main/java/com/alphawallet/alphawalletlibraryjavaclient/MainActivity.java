package com.alphawallet.alphawalletlibraryjavaclient;

import static com.alphawallet.app.service.KeystoreAccountService.KEYSTORE_FOLDER;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alphawallet.app.entity.ImportWalletCallback;
import com.alphawallet.app.entity.SignAuthenticationCallback;
import com.alphawallet.app.entity.Wallet;
import com.alphawallet.app.entity.WalletType;
import com.alphawallet.app.entity.cryptokeys.KeyEncodingType;
import com.alphawallet.app.entity.cryptokeys.SignatureFromKey;
import com.alphawallet.app.service.KeyService;
import com.alphawallet.app.service.KeystoreAccountService;

import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigInteger;

public class MainActivity extends AppCompatActivity
{

    private EditText etSeed;
    private View btnSign;
    private View btnImport;
    private KeyService keyService;
    private TextView tvSig;
    private TextView tvAddress;
    private EditText etMsg;
    private Button btnBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();

        btnImport.setOnClickListener(view -> importSeedPhrase());

        btnSign.setOnClickListener(view -> {
            signMessage();
        });

        btnBrowser.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MyBrowserActivity.class);
            startActivity(intent);
        });
    }

    private void signMessage()
    {
        String address = tvAddress.getText().toString();
        Wallet wallet = new Wallet(address);
        wallet.setWalletType(WalletType.HDKEY);
        keyService.getAuthenticationForSignature(wallet, MainActivity.this, new SignAuthenticationCallback()
        {
            @Override
            public void gotAuthorisation(boolean b)
            {
                File file = new File(getApplicationContext().getFilesDir(), KEYSTORE_FOLDER);
                KeystoreAccountService keystoreAccountService = new KeystoreAccountService(file, getApplicationContext().getFilesDir(), keyService);
                String msg = etMsg.getText().toString();
                SignatureFromKey signatureFromKey = keystoreAccountService.signTransactionEIP1559(wallet, address, BigInteger.ZERO, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, 0, msg.getBytes(), 1L).blockingGet();

                String hash = Numeric.toHexString(signatureFromKey.signature);
                tvSig.setText(hash);
            }

            @Override
            public void cancelAuthentication()
            {

            }
        });

    }

    private void importSeedPhrase()
    {
        String seed = etSeed.getText().toString();
        keyService = new KeyService(getApplication(), null);
        keyService.importHDKey(seed, this, (address, keyEncodingType, authenticationLevel) -> {
            tvAddress.setText(address);
            btnSign.setEnabled(true);
        });
    }

    private void initView()
    {
        btnSign = findViewById(R.id.btn_sign);
        etSeed = findViewById(R.id.etSeed);
        etMsg = findViewById(R.id.etMsg);
        tvAddress = findViewById(R.id.tvAddress);
        btnImport = findViewById(R.id.btn_import);
        tvSig = findViewById(R.id.tvSig);
        btnBrowser = findViewById(R.id.btn_browser);
    }
}