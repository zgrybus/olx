import { createFileRoute } from '@tanstack/react-router';
import { $olxApi } from '@/apis/olx';
import { OfferListItem } from '@/features/offers/offer-list-item/offer-list-item';
import { OffersListSkeleton } from '@/features/offers/offers-list-skeleton/offers-list-skeleton';

export const Route = createFileRoute('/offers/')({
  component: OffersPage,
});

function OffersPage() {
  const { data: offers = [], isPending } = $olxApi.useQuery(
    'get',
    '/api/offers',
  );

  return (
    <main className="py-5 px-2">
      <div className="max-w-[1200px] mx-auto">
        {isPending ? (
          <OffersListSkeleton />
        ) : (
          <div>
            <div className="flex items-baseline justify-between">
              <h2 className="text-xl font-bold tracking-tight text-neutral-900">
                We found {offers.length} offers
              </h2>
            </div>
            <ul className="flex flex-col gap-3 mt-8" aria-label="offers list">
              {offers.map((offer) => (
                <OfferListItem key={offer.id} {...offer} />
              ))}
            </ul>
          </div>
        )}
      </div>
    </main>
  );
}
