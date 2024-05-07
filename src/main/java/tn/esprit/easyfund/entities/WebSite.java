package tn.esprit.easyfund.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebSite {

    private String url;
    private String baliseName;
    private  String baliseDescription;
    private String balisePrice;

    private  String baliseImage;

}

