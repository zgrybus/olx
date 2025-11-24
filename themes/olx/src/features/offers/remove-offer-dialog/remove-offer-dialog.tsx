import { useNavigate } from '@tanstack/react-router';
import { toast } from 'sonner';
import { useQueryClient } from '@tanstack/react-query';
import { $olxApi } from '@/apis/olx';
import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';

type RemoveOfferDialogProps = {
  offerId: number;
};

export function RemoveOfferDialog({ offerId }: RemoveOfferDialogProps) {
  const navigate = useNavigate({ from: '/offers/$offerId' });
  const queryClient = useQueryClient();
  const { mutate: removeOfferMutation, isPending } = $olxApi.useMutation(
    'delete',
    '/api/offers/{offerId}',
    {
      onSuccess: () => {
        // https://github.com/openapi-ts/openapi-typescript/issues/1806
        const queryKey = $olxApi.queryOptions('get', '/api/offers')
          .queryKey as ['get', '/api/offers'];
        queryClient.invalidateQueries({ queryKey });

        toast.success('Your offer has been removed.');
        navigate({ to: '/offers' });
      },
    },
  );

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="destructive">Remove offer</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Are you absolutely sure?</DialogTitle>
          <DialogDescription>
            This action cannot be undone. This will permanently delete your
            offer and remove the data from our servers.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter
          className={`
            gap-2
            sm:justify-end
          `}
        >
          <DialogClose asChild>
            <Button type="button" variant="secondary">
              Cancel
            </Button>
          </DialogClose>
          <Button
            type="button"
            variant="destructive"
            loading={isPending}
            onClick={() =>
              removeOfferMutation({
                params: { path: { offerId } },
              })
            }
          >
            Delete Offer
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
