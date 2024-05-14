package ru.netology.test.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CardData {

  String number;
  String month;
  String year;
  String owner;
  String cvc;
}
