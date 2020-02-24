package br.com.leucotron.desafio.controller;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

public class Mask {

    public void phoneMask(TextInputEditText phoneField){
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) N NNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(phoneField,smf);
        phoneField.addTextChangedListener(mtw);
    }

}
