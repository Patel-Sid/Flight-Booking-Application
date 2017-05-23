
package user;

import java.io.Serializable;
import java.util.Date;

/**
 * An object to represent a <code>CreditCard</code>.
 * 
 * @author jason
 *
 */
public class CreditCard implements Serializable {
  /**
   * ..
   */
  private static final long serialVersionUID = -7413896819866020655L;
  // CreditCardNumber;ExpiryDate
  private String cardNumber;
  private Date expiryDate;

  public CreditCard(String cardNumber, Date expiryDate) {
    this.cardNumber = cardNumber;
    this.expiryDate = expiryDate;
  }

  public String getNumber() {
    return cardNumber;
  }

  public Date getExpiry() {
    return expiryDate;
  }

  public void setNumber(String cardNum) {
    this.cardNumber = cardNum;
  }

  public void setExpiry(Date expiry) {
    this.expiryDate = expiry;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    CreditCard other = (CreditCard) obj;
    return (cardNumber == null ? other.cardNumber == null : cardNumber.equals(other.cardNumber))
        && (expiryDate == null ? other.expiryDate == null : expiryDate.equals(other.expiryDate));
  }
}
