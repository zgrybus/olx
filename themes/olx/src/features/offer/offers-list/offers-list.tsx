import { OfferListItem } from '../offer-list-item/offer-item';
import type { OfferSummary } from '../types';

type OffersListProps = {
  offers: Array<OfferSummary>;
};

export function OffersList({ offers }: OffersListProps) {
  return (
    <div>
      <p className="font-bold text-xl">We found {offers.length} offers</p>
      <ul className="flex flex-col gap-4 mt-8">
        {offers.map((offer) => (
          <OfferListItem key={offer.id} {...offer} />
        ))}
      </ul>
    </div>
  );
}
