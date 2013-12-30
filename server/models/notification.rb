class Notification < ActiveRecord::Base
  belongs_to :user
  belongs_to :location
  belongs_to :territory
  scope :unread, conditions: {read: false}

  def notification_info
    case self.notification_type
    when "entering"
      n.location.to_hash
    when "detection"
      n.territory.to_hash
    end.merge({user_id: n.user.id, user_name: n.user.name})
  end
end
