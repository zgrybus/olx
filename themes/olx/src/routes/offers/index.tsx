import { createFileRoute } from '@tanstack/react-router';
import { OffersList } from '@/features/offers/offers-list/offers-list';

export const Route = createFileRoute('/offers/')({
  component: Offers,
});

function Offers() {
  return (
    <main className="py-5 px-2">
      <div className="max-w-[1200px] mx-auto">
        <OffersList />
      </div>
    </main>
  );
}
