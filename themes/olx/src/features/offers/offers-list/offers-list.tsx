import { OfferItem } from '../offer-item/offer-item';
import { OffersListSkeleton } from '../offers-list-skeleton/offers-list-skeleton';
import { $olxApi } from '@/apis/olx';

export function OffersList() {
  const { data: offers = [], isPending } = $olxApi.useQuery(
    'get',
    '/api/offers',
  );

  if (isPending) {
    return <OffersListSkeleton />;
  }

  return (
    <div>
      <p className="font-bold text-xl">We found {offers.length} offers</p>
      <ul className="flex flex-col gap-4 mt-8">
        {offers.map((offer) => (
          <OfferItem key={offer.id} {...offer} />
        ))}
      </ul>
    </div>
  );
}
