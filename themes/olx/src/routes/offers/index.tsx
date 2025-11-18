import { createFileRoute } from '@tanstack/react-router';
import { OffersList } from '@/features/offer/offers-list/offers-list';
import { $olxApi } from '@/apis/olx';
import { OffersListSkeleton } from '@/features/offer/offers-list-skeleton/offers-list-skeleton';

export const Route = createFileRoute('/offers/')({
  component: Offers,
});

function Offers() {
  const { data: offers = [], isPending } = $olxApi.useQuery(
    'get',
    '/api/offers',
  );

  if (isPending) {
    return <OffersListSkeleton />;
  }

  return (
    <main className="py-5 px-2">
      <div className="max-w-[1200px] mx-auto">
        <OffersList offers={offers} />
      </div>
    </main>
  );
}
