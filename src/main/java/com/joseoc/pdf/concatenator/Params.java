package com.joseoc.pdf.concatenator;

import lombok.Data;

import java.util.List;

@Data
final class Params {

    private List<String> inputs;
    private String output;

}
