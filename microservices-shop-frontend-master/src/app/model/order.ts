export interface Order {
  id?: number;
  orderNumber?: "macbook_pro_16";
  skuCode: string;
  price: number;
  quantity: number;
  userDetails: UserDetails
}

export interface UserDetails {
  email: string;
  firstName: string;
  lastName: string;
}
