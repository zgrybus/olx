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
    <main className="min-h-screen px-42 py-6">
      <div className="mx-auto max-w-[1200px]">
        {isPending ? (
          <OffersListSkeleton />
        ) : (
          <div>
            <div className="flex items-baseline justify-between">
              <h2 className="text-xl font-bold tracking-tight text-neutral-900">
                We found {offers.length} offers
              </h2>
            </div>
            <ul className="mt-8 flex flex-col gap-3" aria-label="offers list">
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
