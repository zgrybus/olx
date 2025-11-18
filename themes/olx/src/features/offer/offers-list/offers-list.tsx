import { OfferListItem } from '../offer-list-item/offer-item';
import type { OfferSummary } from '../types';

type OffersListProps = {
  offers: Array<OfferSummary>;
};

export function OffersList({ offers }: OffersListProps) {
  return (
    <div className="space-y-6">
      <div className="flex items-baseline justify-between">
        <h2 className="text-xl font-bold tracking-tight text-neutral-900">
          We found {offers.length} offers
        </h2>
      </div>
      <ul className="flex flex-col gap-3">
        {offers.map((offer) => (
          <OfferListItem key={offer.id} {...offer} />
        ))}
      </ul>
    </div>
  );
}
