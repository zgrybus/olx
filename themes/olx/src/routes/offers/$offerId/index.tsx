import { Link, createFileRoute } from '@tanstack/react-router';
import { ChevronLeft } from 'lucide-react';
import { $olxApi } from '@/apis/olx';
import { Button } from '@/components/ui/button';
import { OfferDetailsSkeleton } from '@/features/offers/offer-details-skeleton/offer-details-skeleton';
import { OfferDetailsSummaryCard } from '@/features/offers/offer-details-summary-card/offer-details-summary-card';
import { OfferDetailsDescriptionCard } from '@/features/offers/offer-details-description-card/offer-details-description-card';
import { RemoveOfferDialog } from '@/features/offers/remove-offer-dialog/remove-offer-dialog';

export const Route = createFileRoute('/offers/$offerId/')({
  component: OfferItemDetails,
});

function OfferItemDetails() {
  const offerId = Route.useParams({
    select: (params) => parseInt(params.offerId),
  });

  const { data: offer, isPending } = $olxApi.useQuery(
    'get',
    '/api/offers/{offerId}',
    { params: { path: { offerId } } },
    { enabled: !!offerId },
  );

  return (
    <main className="min-h-screen py-6 px-4">
      <div className="max-w-[1200px] mx-auto">
        <div className="flex justify-between">
          <Button
            variant="ghost"
            asChild
            className="hover:bg-transparent hover:text-neutral-600 text-neutral-800"
          >
            <Link to="/offers">
              <ChevronLeft className="h-4 w-4" />
              Go back
            </Link>
          </Button>
          <RemoveOfferDialog offerId={offerId} />
        </div>
        {isPending ? (
          <OfferDetailsSkeleton />
        ) : (
          <div className="flex flex-col gap-3 md:flex-row mt-6">
            <div className="md:w-1/3 md:order-2">
              <OfferDetailsSummaryCard
                title={offer?.title}
                createdAt={offer?.createdAt}
                price={offer?.price}
              />
            </div>
            <div className="md:w-2/3">
              <OfferDetailsDescriptionCard
                id={offer?.id}
                description={offer?.description}
              />
            </div>
          </div>
        )}
      </div>
    </main>
  );
}
