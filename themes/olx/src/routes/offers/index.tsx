import { createFileRoute } from '@tanstack/react-router';
import { OfferItem } from '@/features/offers/offer-item/offer-item';
import { $olxApi } from '@/apis/olx';

export const Route = createFileRoute('/offers/')({
  component: Offers,
});

function Offers() {
  const { data: offers = [] } = $olxApi.useQuery('get', '/api/offers');

  return (
    <main className="py-5 px-2">
      <div className="max-w-[1200px] mx-auto">
        <p className="font-bold text-xl">We found {offers.length} offers</p>
        <ul className="flex flex-col gap-4 mt-8">
          {offers.map((offer) => (
            <OfferItem key={offer.id} {...offer} />
          ))}
        </ul>
      </div>
    </main>
  );
}
