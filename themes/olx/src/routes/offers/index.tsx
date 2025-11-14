/* eslint-disable @typescript-eslint/no-explicit-any */
import { createFileRoute } from '@tanstack/react-router';
import { useQuery } from '@tanstack/react-query';
import { OfferItem } from '@/features/offers/offer-item/offer-item';

export const Route = createFileRoute('/offers/')({
  component: Offers,
});

function Offers() {
  const { data: offers = [] } = useQuery({
    queryFn: async () => {
      const response = await window.fetch('http://localhost:8080/api/offers');
      return await response.json();
    },
    queryKey: ['ds'],
  });

  return (
    <main className="py-5 px-2 text-xl font-bold bg-zinc-100">
      <div className="max-w-[1200px] mx-auto">
        <p>We found {offers.length} offers</p>
        <ul className="flex flex-col gap-4 mt-8">
          {offers.map((offer: any) => (
            <OfferItem key={offer.title} {...offer} />
          ))}
        </ul>
      </div>
    </main>
  );
}
